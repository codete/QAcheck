package com.codete.regression.testengine.comparisonsettings;

import com.codete.regression.api.testengine.TestRunConfig;
import com.codete.regression.testengine.teststep.ComparisonResult;
import com.codete.regression.testengine.teststep.TestStep;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import org.junit.jupiter.api.Test;

import static com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum.BASELINE_ACCEPTED;
import static com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum.CURRENT_ACCEPTED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComparisonSettingsServiceTest {

    private final ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);
    private final ComparisonSettingsRepository comparisonSettingsRepository = mock(ComparisonSettingsRepository.class);
    private final ComparisonSettingsService comparisonSettingsService = new ComparisonSettingsService(comparisonSettingsRepository);

    @Test
    void shouldSetNewBaselineWhenUserAcceptedCurrent() {
        TestStep testStep = mockTestStep("baseline", "current");

        comparisonSettingsService.applyUserDecision(comparisonSettings, CURRENT_ACCEPTED, testStep);

        verify(comparisonSettings).setBaselineScreenshotPath("current");
        verify(comparisonSettingsRepository).save(comparisonSettings);
    }

    @Test
    void shouldOverrideBaselineWhenUserAcceptedBaseline() {
        when(comparisonSettings.getBaselineScreenshotPath()).thenReturn("currentBaseline");

        TestStep testStep = mockTestStep("newBaseline", "current");

        comparisonSettingsService.applyUserDecision(comparisonSettings, BASELINE_ACCEPTED, testStep);

        verify(comparisonSettings).setBaselineScreenshotPath("newBaseline");
        verify(comparisonSettingsRepository).save(comparisonSettings);
    }

    @Test
    void shouldCreateNewComparisonSettingsWhenTheyDoNotExist() {
        TestRunConfig testRunConfig = mock(TestRunConfig.class);
        when(testRunConfig.getAllowedDifferencePercentage()).thenReturn(50.0);

        ComparisonSettings comparisonSettings =
                comparisonSettingsService.getComparisonSettings(null, testRunConfig);

        assertThat(comparisonSettings.getAllowedDifferencePercentage(),
                is(testRunConfig.getAllowedDifferencePercentage()));
    }

    @Test
    void shouldUseExistingSettingsWhenTheyAreTheSame() {
        double threshold = 50.0;
        ComparisonSettings existingComparisonSettings = mock(ComparisonSettings.class);
        when(existingComparisonSettings.getAllowedDifferencePercentage()).thenReturn(threshold);
        TestStepConfig testStepConfig = mock(TestStepConfig.class);
        when(testStepConfig.getComparisonSettings()).thenReturn(existingComparisonSettings);
        TestRunConfig testRunConfig = mock(TestRunConfig.class);
        when(testRunConfig.getAllowedDifferencePercentage()).thenReturn(threshold);

        ComparisonSettings comparisonSettings =
                comparisonSettingsService.getComparisonSettings(testStepConfig, testRunConfig);

        assertThat(comparisonSettings, is(existingComparisonSettings));
    }

    @Test
    void shouldCreateNewSettingsWhenTheyAreDifferent() {
        ComparisonSettings existingComparisonSettings = mock(ComparisonSettings.class);
        when(existingComparisonSettings.getAllowedDifferencePercentage()).thenReturn(40.0);
        TestStepConfig testStepConfig = mock(TestStepConfig.class);
        when(testStepConfig.getComparisonSettings()).thenReturn(existingComparisonSettings);
        TestRunConfig testRunConfig = mock(TestRunConfig.class);
        when(testRunConfig.getAllowedDifferencePercentage()).thenReturn(50.0);

        ComparisonSettings comparisonSettings =
                comparisonSettingsService.getComparisonSettings(testStepConfig, testRunConfig);

        assertThat(comparisonSettings, is(not((existingComparisonSettings))));
        assertThat(comparisonSettings.getAllowedDifferencePercentage(),
                is(testRunConfig.getAllowedDifferencePercentage()));
    }


    private TestStep mockTestStep(String baselineScreenshotPath, String currentScreenshotPath) {
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);
        ComparisonResult comparisonResult = mock(ComparisonResult.class);
        when(comparisonSettings.getBaselineScreenshotPath()).thenReturn(baselineScreenshotPath);
        when(comparisonResult.getCurrentScreenshotPath()).thenReturn(currentScreenshotPath);
        TestStep testStep = mock(TestStep.class);
        when(testStep.getComparisonResult()).thenReturn(comparisonResult);
        when(testStep.getComparisonSettings()).thenReturn(comparisonSettings);
        return testStep;
    }

}