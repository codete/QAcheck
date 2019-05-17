package com.codete.regression.testengine.teststepconfig;

import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.teststep.TestStep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestStepConfigService {

    private final TestStepConfigRepository testStepConfigRepository;

    public TestStepConfigService(TestStepConfigRepository testStepConfigRepository) {
        this.testStepConfigRepository = testStepConfigRepository;
    }

    @Transactional
    public TestStepConfig initializeTestStepConfigIfTestStepWasRunFirstTime(TestCase testCase, TestStep testStep) {
        TestStepConfig testStepConfig = createNewTestStepConfig(testCase, testStep);
        return testStepConfigRepository.save(testStepConfig);
    }

    private TestStepConfig createNewTestStepConfig(TestCase testCase, TestStep testStep) {
        TestStepConfig testStepConfig = new TestStepConfig();
        ComparisonSettings comparisonSettings = new ComparisonSettings(testStep.getComparisonSettings());
        comparisonSettings.setBaselineScreenshotPath(testStep.getComparisonResult().getCurrentScreenshotPath());
        comparisonSettings.setAllowedDifferencePercentage(0.0);
        testStepConfig.setComparisonSettings(comparisonSettings);
        testStepConfig.setStepName(testStep.getStepName());
        testStepConfig.setTestCase(testCase);
        return testStepConfig;
    }
}
