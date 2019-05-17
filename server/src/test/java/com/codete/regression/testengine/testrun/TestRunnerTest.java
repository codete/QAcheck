package com.codete.regression.testengine.testrun;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.testengine.TestRunRequest;
import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsService;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.teststep.TestStep;
import com.codete.regression.testengine.teststep.TestStepRunner;
import com.codete.regression.testengine.teststepconfig.TestStepConfigService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TestRunnerTest {

    private final TestStepRunner testStepRunner = mock(TestStepRunner.class);
    private final ScreenshotStorage screenshotStorage = mock(ScreenshotStorage.class);
    private final TestStepConfigService testStepConfigService = mock(TestStepConfigService.class);
    private final ComparisonSettingsService comparisonSettingsService = mock(ComparisonSettingsService.class);
    private final TestRunner testRunner = new TestRunner(testStepRunner, screenshotStorage,
            testStepConfigService, comparisonSettingsService);

    @Test
    void shouldRunTestStepForEachScreenshot() {
        Screenshot firstScreenshot = mock(Screenshot.class);
        Screenshot secondScreenshot = mock(Screenshot.class);
        TestRunRequest testRunRequest = mock(TestRunRequest.class);
        TestStep testStepForFirstScreenshot = mock(TestStep.class);
        TestStep testStepForSecondScreenshot = mock(TestStep.class);
        when(testRunRequest.getScreenshots()).thenReturn(Arrays.asList(firstScreenshot, secondScreenshot));
        when(testStepRunner.runTestStep(any(), any(), eq(firstScreenshot))).thenReturn(testStepForFirstScreenshot);
        when(testStepRunner.runTestStep(any(), any(), eq(secondScreenshot))).thenReturn(testStepForSecondScreenshot);

        List<TestStep> testSteps = testRunner.runComparisons(mock(TestCase.class), mock(TestRun.class), testRunRequest);

        assertThat(testSteps, hasSize(2));
    }

    @Test
    void shouldInitTestStepConfigWhenTestStepIsRunForTheFirstTime() {
        Screenshot screenshot = mock(Screenshot.class);
        TestRunRequest testRunRequest = mock(TestRunRequest.class);
        TestStep testStep = mock(TestStep.class);
        TestCase testCase = mock(TestCase.class);
        when(testRunRequest.getScreenshots()).thenReturn(Collections.singletonList(screenshot));
        when(testStepRunner.runTestStep(any(), any(), eq(screenshot))).thenReturn(testStep);

        testRunner.runComparisons(testCase, mock(TestRun.class), testRunRequest);

        verify(testStepConfigService).initializeTestStepConfigIfTestStepWasRunFirstTime(testCase, testStep);
    }

}