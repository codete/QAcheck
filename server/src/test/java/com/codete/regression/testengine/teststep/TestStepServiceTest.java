package com.codete.regression.testengine.teststep;

import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsDto;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsService;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestStepServiceTest {

    private final TestStepRepository testStepRepository = mock(TestStepRepository.class);
    private final ComparisonSettingsService comparisonSettingsService = mock(ComparisonSettingsService.class);
    private final ArgumentCaptor<ComparisonSettings> comparisonSettingsArgumentCaptor
            = ArgumentCaptor.forClass(ComparisonSettings.class);

    @InjectMocks
    private TestStepService testStepService;

    @Test
    void shouldCopyComparisonSettingsIfTheyAreShared() {
        Long testStepId = 10L;
        Long settingsId = 1L;
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);
        ComparisonResult comparisonResult = mock(ComparisonResult.class);
        ComparisonSettingsDto comparisonSettingsDto = new ComparisonSettingsDto();
        TestStep testStep = mockTestStep(testStepId, comparisonSettings, comparisonSettings, comparisonResult);
        when(comparisonSettings.getId()).thenReturn(settingsId);
        when(testStepRepository.findById(testStepId)).thenReturn(Optional.of(testStep));
        when(testStepRepository.saveAndFlush(testStep)).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(testStepRepository.countByComparisonSettingsIdAndIdNot(settingsId, testStepId)).thenReturn(1L);

        testStepService.saveComparisonSettings(testStepId, comparisonSettingsDto);

        verify(comparisonSettingsService, times(1)).mergeComparisonSettings(
                comparisonSettingsArgumentCaptor.capture(), eq(comparisonSettingsDto));
        ComparisonSettings comparisonSettingsResult = comparisonSettingsArgumentCaptor.getValue();

        assertThat(comparisonSettingsResult, not(comparisonSettings));
        assertThat(testStep.getTestStepConfig().getComparisonSettings(), is(testStep.getComparisonSettings()));
    }

    @Test
    void shouldNotCopyComparisonSettingsIfTheyAreNotShared() {
        Long testStepId = 10L;
        Long settingsId = 1L;
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);
        ComparisonResult comparisonResult = mock(ComparisonResult.class);
        ComparisonSettingsDto comparisonSettingsDto = new ComparisonSettingsDto();
        TestStep testStep = mockTestStep(testStepId, comparisonSettings, comparisonSettings, comparisonResult);
        when(comparisonSettings.getId()).thenReturn(settingsId);
        when(testStepRepository.findById(testStepId)).thenReturn(Optional.of(testStep));
        when(testStepRepository.saveAndFlush(testStep)).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(testStepRepository.countByComparisonSettingsIdAndIdNot(settingsId, testStepId)).thenReturn(0L);

        testStepService.saveComparisonSettings(testStepId, comparisonSettingsDto);

        verify(comparisonSettingsService, times(1)).mergeComparisonSettings(
                comparisonSettingsArgumentCaptor.capture(), eq(comparisonSettingsDto));
        ComparisonSettings comparisonSettingsResult = comparisonSettingsArgumentCaptor.getValue();

        assertThat(comparisonSettingsResult, is(comparisonSettings));
        assertThat(testStep.getTestStepConfig().getComparisonSettings(), is(testStep.getComparisonSettings()));
    }

    private TestStep mockTestStep(Long testStepId,
                                  ComparisonSettings testStepComparisonSettings,
                                  ComparisonSettings testStepConfigComparisonSettings,
                                  ComparisonResult comparisonResult) {
        TestStep testStep = new TestStep();
        testStep.setId(testStepId);
        TestStepConfig testStepConfig = new TestStepConfig();
        testStepConfig.setComparisonSettings(testStepConfigComparisonSettings);
        testStep.setComparisonSettings(testStepComparisonSettings);
        testStep.setTestStepConfig(testStepConfig);
        testStep.setComparisonResult(comparisonResult);
        return testStep;
    }

}