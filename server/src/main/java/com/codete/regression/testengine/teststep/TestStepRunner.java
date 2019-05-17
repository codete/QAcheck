package com.codete.regression.testengine.teststep;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.ScreenshotComparator;
import com.codete.regression.screenshot.dynamicareas.DynamicAreaDetector;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import com.codete.regression.testengine.userdecision.UserDecision;
import com.codete.regression.testengine.userdecision.UserDecisionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum.NO_DECISION;

@Service
@Slf4j
public class TestStepRunner {

    private final UserDecisionService userDecisionService;
    private final ScreenshotComparator screenshotComparator;
    private final DynamicAreaDetector dynamicAreaDetector;

    public TestStepRunner(UserDecisionService userDecisionService, ScreenshotComparator screenshotComparator,
                          DynamicAreaDetector dynamicAreaDetector) {
        this.userDecisionService = userDecisionService;
        this.screenshotComparator = screenshotComparator;
        this.dynamicAreaDetector = dynamicAreaDetector;
    }

    public TestStep runTestStep(ComparisonSettings comparisonSettings, String screenshotStorageLocation,
                                Screenshot screenshot) {
        ComparisonResult comparisonResult = screenshotComparator.saveAndCompareScreenshotWithTheBaseline(
                comparisonSettings, screenshotStorageLocation, screenshot);
        return createTestStep(screenshot.getStepName(), comparisonSettings, comparisonResult);
    }

    public TestStep runTestStepWithDynamicAreasDetection(ComparisonSettings comparisonSettings,
                                                         String screenshotStorageLocation,
                                                         List<Screenshot> screenshots) {
        List<IgnoreArea> generatedIgnoreAreas = dynamicAreaDetector.createIgnoreAreasForDynamicElements(
                comparisonSettings, screenshots);
        log.info("Ignore areas for dynamic elements calculated.");
        if (!generatedIgnoreAreas.isEmpty()) {
            //We are creating comparison settings copy,
            //because we don't want to add new ignore areas to settings already stored in db.
            ComparisonSettings comparisonSettingsWithGeneratedAreas = new ComparisonSettings(comparisonSettings);
            generatedIgnoreAreas.forEach(ignoreArea -> ignoreArea.setComparisonSettings(comparisonSettingsWithGeneratedAreas));
            comparisonSettingsWithGeneratedAreas.getIgnoreAreas().addAll(generatedIgnoreAreas);
            comparisonSettings = comparisonSettingsWithGeneratedAreas;
        }
        return runTestStep(comparisonSettings, screenshotStorageLocation, screenshots.get(0));
    }

    private TestStep createTestStep(String stepName, ComparisonSettings comparisonSettings,
                                    ComparisonResult comparisonResult) {
        TestStep testStep = new TestStep();
        testStep.setStepName(stepName);
        testStep.setComparisonResult(comparisonResult);
        testStep.setComparisonSettings(comparisonSettings);
        UserDecision defaultUserDecision = userDecisionService.findByValue(NO_DECISION);
        testStep.setUserDecision(defaultUserDecision);
        return testStep;
    }


}
