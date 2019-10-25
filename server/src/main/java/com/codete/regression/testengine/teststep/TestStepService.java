package com.codete.regression.testengine.teststep;

import com.codete.regression.screenshot.ImageIOWrapper;
import com.codete.regression.screenshot.ScreenshotBufferedImage;
import com.codete.regression.screenshot.ScreenshotComparator;
import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsDto;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsService;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.testrun.TestRunService;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import com.codete.regression.testengine.teststepdetails.TestStepDetailsDto;
import com.codete.regression.testengine.teststepdetails.TestStepDetailsFactory;
import com.codete.regression.testengine.userdecision.UserDecision;
import com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum;
import com.codete.regression.testengine.userdecision.UserDecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum.CURRENT_ACCEPTED;

@Service
@RequiredArgsConstructor
public class TestStepService {
    private final TestRunService testRunService;
    private final TestStepRepository testStepRepository;
    private final ComparisonSettingsService comparisonSettingsService;
    private final UserDecisionService userDecisionService;
    private final TestStepDetailsFactory testStepDetailsFactory;
    private final ScreenshotComparator screenshotComparator;
    private final ScreenshotStorage screenshotStorage;
    private final ImageIOWrapper imageIOWrapper;

    @Transactional
    public boolean repeatComparison(long testStepId) {
        TestStep testStep = getTestStepById(testStepId);
        ComparisonResult comparisonResult = screenshotComparator.compareTestStepScreenshotWithTheBaseline(testStep);
        testStep.setComparisonResult(comparisonResult);
        testStepRepository.save(testStep);
        testRunService.updateTestRunAggregatedStatusIfNecessary(testStep.getTestRun());
        return comparisonResult.isPassed();
    }

    @Transactional
    public void saveComparisonSettings(long testStepId, ComparisonSettingsDto comparisonSettings) {
        TestStep testStep = getTestStepById(testStepId);
        TestStepConfig testStepConfig = testStep.getTestStepConfig();
        transformStepSettingsIntoConfigSettings(testStep, testStepConfig);
        comparisonSettingsService.mergeComparisonSettings(
                testStep.getComparisonSettings(),
                comparisonSettings);
    }

    private boolean isComparisonSettingsShared(long testStepId, ComparisonSettings stepSettings) {
        return testStepRepository.countByComparisonSettingsIdAndIdNot(stepSettings.getId(), testStepId) > 0;
    }

    @Transactional
    public boolean saveUserDecision(long testStepId, UserDecisionEnum userDecision) {
        return testStepRepository.findById(testStepId)
                .filter(testStep -> testStep.getComparisonSettings().getBaselineScreenshotPath() != null)
                .map(testStep -> {
                    applyUserDecision(testStep, userDecision);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<TestStep> getStepsForTestRunUuid(String testRunUuid) {
        return testStepRepository.findAllByTestRunUuid(testRunUuid);
    }

    @Transactional
    public TestStepDetailsDto getTestStepDetails(long testStepId) {
        TestStep testStep = getTestStepById(testStepId);
        return testStepDetailsFactory.buildTestStepDetailsDto(testStep);
    }

    @Transactional
    public boolean doesUserHaveAccess(long testRunId, String username) {
        return testStepRepository.findById(testRunId)
                .map(testStep -> testStep.getUsername().equals(username))
                .orElse(false);
    }

    @Transactional
    public boolean runComparison(long testStepId, MultipartFile image) {
        TestStep testStep = getTestStepById(testStepId);

        ScreenshotBufferedImage uploadedBaseline = saveUploadedBaseline(image, testStep);
        updateComparisonSettings(testStep, uploadedBaseline);

        ComparisonResult comparisonResult = screenshotComparator.compareTestStepScreenshotWithTheBaseline(testStep);
        testStep.setComparisonResult(comparisonResult);
        testStepRepository.save(testStep);
        testRunService.updateTestRunAggregatedStatusIfNecessary(testStep.getTestRun());

        return comparisonResult.isPassed();
    }

    private TestStep getTestStepById(Long testStepId) {
        return testStepRepository.findById(testStepId)
                .orElseThrow(() -> new IllegalArgumentException("TestStep with id=" + testStepId + " does not exists."));
    }

    private void applyUserDecision(TestStep testStep, UserDecisionEnum userDecision) {
        UserDecision dbUserDecision = userDecisionService.findByValue(userDecision);
        testStep.setUserDecision(dbUserDecision);
        TestCase testCase = testStep.getTestRun().getTestCase();
        TestStepConfig testStepConfig = testCase.getStepConfigForStepName(testStep.getStepName());
        boolean testPassed = userDecision == CURRENT_ACCEPTED;
        testStep.getComparisonResult().setPassed(testPassed);
        transformStepSettingsIntoConfigSettings(testStep, testStepConfig);
        comparisonSettingsService.applyUserDecision(testStepConfig.getComparisonSettings(), userDecision, testStep);
        testRunService.updateTestRunAggregatedStatusIfNecessary(testStep.getTestRun());
        testStepRepository.save(testStep);
    }

    private void transformStepSettingsIntoConfigSettings(TestStep testStep, TestStepConfig testStepConfig) {
        ComparisonSettings stepSettings = testStep.getComparisonSettings();
        if (isComparisonSettingsShared(testStep.getId(), testStep.getComparisonSettings())) {
            stepSettings = createNewComparisonSettingsForTestStep(testStep);
        }
        if (stepSettings.getBaselineScreenshotPath() == null) {
            ComparisonResult comparisonResult = testStep.getComparisonResult();
            stepSettings.setBaselineScreenshotPath(comparisonResult.getCurrentScreenshotPath());
        }

        testStepConfig.setComparisonSettings(stepSettings);
    }

    private ComparisonSettings createNewComparisonSettingsForTestStep(TestStep testStep) {
        ComparisonSettings newComparisonSettings = new ComparisonSettings(testStep.getComparisonSettings());
        testStep.setComparisonSettings(newComparisonSettings);
        //We call saveAndFlush to properly handle comparisonSettings.getIgnoreAreas().clear() code
        //which will be invoked in next steps. Without saveAndFlush comparisonSettings.getIgnoreAreas().clear()
        //will not delete previous ignore areas.
        testStep = testStepRepository.saveAndFlush(testStep);
        return testStep.getComparisonSettings();
    }

    private void updateComparisonSettings(TestStep testStep, ScreenshotBufferedImage uploadedScreenShot) {
        ComparisonSettings oldComparisonSettings = testStep.getComparisonSettings();
        oldComparisonSettings.setBaselineScreenshotPath(uploadedScreenShot.getRelativePath());
        ComparisonSettings newComparisonSettingsForTestStep = createNewComparisonSettingsForTestStep(testStep);
        testStep.getTestStepConfig().setComparisonSettings(newComparisonSettingsForTestStep);
    }

    private ScreenshotBufferedImage saveUploadedBaseline(MultipartFile image, TestStep testStep) {
        TestCase testCase = testStep.getTestRun().getTestCase();
        String uploadedScreenshotStorageLocation = screenshotStorage.getUploadedScreenshotStorageLocation(testCase);
        String fileName = testStep.getStepName();
        screenshotStorage.deleteByFileName(uploadedScreenshotStorageLocation, fileName);
        try {
            BufferedImage uploadedBaseline = imageIOWrapper.convertToBufferedImage(image.getBytes());
            return screenshotStorage.saveScreenshot(uploadedBaseline, uploadedScreenshotStorageLocation, fileName);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to open uploaded file!", ex);
        }
    }

}
