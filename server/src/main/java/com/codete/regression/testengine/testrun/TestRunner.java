package com.codete.regression.testengine.testrun;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.testengine.TestRunConfig;
import com.codete.regression.api.testengine.TestRunRequest;
import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsService;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.teststep.TestStep;
import com.codete.regression.testengine.teststep.TestStepRunner;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import com.codete.regression.testengine.teststepconfig.TestStepConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
class TestRunner {

    private final TestStepRunner testStepRunner;
    private final ScreenshotStorage screenshotStorage;
    private final TestStepConfigService testStepConfigService;
    private final ComparisonSettingsService comparisonSettingsService;

    TestRunner(TestStepRunner testStepRunner, ScreenshotStorage screenshotStorage,
               TestStepConfigService testStepConfigService, ComparisonSettingsService comparisonSettingsService) {
        this.testStepRunner = testStepRunner;
        this.screenshotStorage = screenshotStorage;
        this.testStepConfigService = testStepConfigService;
        this.comparisonSettingsService = comparisonSettingsService;
    }

    @Transactional
    List<TestStep> runComparisons(TestCase testCase, TestRun testRun, TestRunRequest request) {
        String screenshotStorageLocation = screenshotStorage.getScreenshotStorageLocation(testCase, testRun, request);
        registerTransactionRolledBackCallback(screenshotStorageLocation);
        List<TestStep> testSteps;
        if (request.isDetectDynamicElements()) {
            testSteps = runComparisonsWithDynamicAreasDetection(testCase, request, screenshotStorageLocation);
        } else {
            testSteps = runComparisonsWithoutDynamicAreasDetection(testCase, request, screenshotStorageLocation);
        }
        testSteps.forEach(testStep -> testStep.setTestRun(testRun));
        return testSteps;
    }

    private void registerTransactionRolledBackCallback(String screenshotStorageLocation) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        log.info("Transaction was rolled back, removing created files.");
                        screenshotStorage.deleteScreenshots(screenshotStorageLocation);
                    }
                }
            });
        }
    }

    private List<TestStep> runComparisonsWithDynamicAreasDetection(TestCase testCase,
                                                                   TestRunRequest request,
                                                                   String screenshotStorageLocation) {
        Map<String, List<Screenshot>> screenshotsGroupedByStepName = groupScreenshotsByStepName(request);
        TestRunConfig comparatorConfig = request.getTestRunConfig();
        List<TestStep> testSteps = new ArrayList<>();
        for (List<Screenshot> screenshots : screenshotsGroupedByStepName.values()) {
            String stepName = screenshots.get(0).getStepName();
            log.info("Running test step {} with dynamic areas detection.", stepName);
            TestStepConfig testStepConfig = testCase.getStepConfigForStepName(stepName);
            ComparisonSettings comparisonSettings = comparisonSettingsService.getComparisonSettings(testStepConfig,
                    comparatorConfig);
            TestStep testStep = testStepRunner.runTestStepWithDynamicAreasDetection(
                    comparisonSettings, screenshotStorageLocation, screenshots);
            testSteps.add(setTestStepConfig(testStep, testCase, testStepConfig));
        }
        return testSteps;
    }

    private List<TestStep> runComparisonsWithoutDynamicAreasDetection(TestCase testCase,
                                                                      TestRunRequest request,
                                                                      String screenshotStorageLocation) {
        TestRunConfig comparatorConfig = request.getTestRunConfig();
        List<TestStep> testSteps = new ArrayList<>();
        for (Screenshot screenshot : request.getScreenshots()) {
            log.info("Running test step {} without dynamic areas detection.", screenshot.getStepName());
            TestStepConfig testStepConfig = testCase.getStepConfigForStepName(screenshot.getStepName());
            ComparisonSettings comparisonSettings = comparisonSettingsService.getComparisonSettings(testStepConfig,
                    comparatorConfig);
            TestStep testStep = testStepRunner.runTestStep(comparisonSettings, screenshotStorageLocation, screenshot);
            testSteps.add(setTestStepConfig(testStep, testCase, testStepConfig));
        }
        return testSteps;
    }

    private Map<String, List<Screenshot>> groupScreenshotsByStepName(TestRunRequest request) {
        return request.getScreenshots()
                .stream()
                .collect(Collectors.groupingBy(Screenshot::getStepName, LinkedHashMap::new, Collectors.toList()));
    }

    private TestStep setTestStepConfig(TestStep testStep, TestCase testCase, TestStepConfig testStepConfig) {
        if (testStepConfig == null) {
            testStepConfig = testStepConfigService.initializeTestStepConfigIfTestStepWasRunFirstTime(testCase, testStep);
        }
        testStep.setTestStepConfig(testStepConfig);
        return testStep;
    }
}
