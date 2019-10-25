package com.codete.regression.testengine.teststep;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.ImageIOWrapper;
import com.codete.regression.screenshot.ScreenshotBufferedImage;
import com.codete.regression.screenshot.ScreenshotComparator;
import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsDto;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsService;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.testrun.TestRun;
import com.codete.regression.testengine.testrun.TestRunService;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
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
    private final ScreenshotStorage screenshotStorage = mock(ScreenshotStorage.class);
    private final ImageIOWrapper imageIOWrapper = mock(ImageIOWrapper.class);
    private final TestRunService testRunService = mock(TestRunService.class);
    private final ScreenshotComparator screenshotComparator = mock(ScreenshotComparator.class);

    @InjectMocks
    private TestStepService testStepService;

    @Test
    void shouldCopyComparisonSettingsIfTheyAreShared() {
        Long testStepId = 10L;
        Long settingsId = 1L;
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);
        ComparisonResult comparisonResult = mock(ComparisonResult.class);
        ComparisonSettingsDto comparisonSettingsDto = new ComparisonSettingsDto();
        TestStep testStep = mockTestStep(testStepId, StringUtils.EMPTY, comparisonSettings, comparisonSettings, comparisonResult, mock(TestRun.class));
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
        TestStep testStep = mockTestStep(testStepId, StringUtils.EMPTY, comparisonSettings, comparisonSettings, comparisonResult, mock(TestRun.class));
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

    @Test
    @SneakyThrows
    void shouldDeleteAndSaveUploadedBaselineScreenshot() {
        long testStepId = 1L;
        String stepName = "step1";
        String uploadedScreenshotLocation = "/location";
        MultipartFile multipartFile = mock(MultipartFile.class);
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);
        ComparisonResult comparisonResult = mock(ComparisonResult.class);
        TestRun testRun = mock(TestRun.class);
        TestStep mockedTestStep = mockTestStep(testStepId, stepName, comparisonSettings, comparisonSettings, comparisonResult, testRun);

        when(testRun.getTestCase()).thenReturn(mock(TestCase.class));
        when(multipartFile.getBytes()).thenReturn(new byte[]{});
        when(testStepRepository.findById(eq(testStepId))).thenReturn(Optional.of(mockedTestStep));
        when(testStepRepository.saveAndFlush(eq(mockedTestStep))).thenReturn(mockedTestStep);
        when(screenshotStorage.getUploadedScreenshotStorageLocation(any(TestCase.class))).thenReturn(uploadedScreenshotLocation);
        when(imageIOWrapper.convertToBufferedImage(any())).thenReturn(mock(BufferedImage.class));
        when(screenshotStorage.saveScreenshot(any(BufferedImage.class), eq(uploadedScreenshotLocation), eq(stepName)))
                .thenReturn(new ScreenshotBufferedImage("/location/step1.png", new BufferedImage(1, 1, 1)));
        when(screenshotComparator.compareTestStepScreenshotWithTheBaseline(eq(mockedTestStep)))
                .thenReturn(comparisonResult);
        when(comparisonResult.isPassed()).thenReturn(true);

        boolean result = testStepService.runComparison(testStepId, multipartFile);

        verify(screenshotStorage).deleteByFileName(eq(uploadedScreenshotLocation), eq(stepName));
        assertThat(result, is(Boolean.TRUE));
    }

    private TestStep mockTestStep(Long testStepId,
                                  String stepName,
                                  ComparisonSettings testStepComparisonSettings,
                                  ComparisonSettings testStepConfigComparisonSettings,
                                  ComparisonResult comparisonResult,
                                  TestRun testRun) {
        TestStep testStep = new TestStep();
        testStep.setId(testStepId);
        testStep.setStepName(stepName);
        testStep.setTestRun(testRun);
        TestStepConfig testStepConfig = new TestStepConfig();
        testStepConfig.setComparisonSettings(testStepConfigComparisonSettings);
        testStep.setComparisonSettings(testStepComparisonSettings);
        testStep.setTestStepConfig(testStepConfig);
        testStep.setComparisonResult(comparisonResult);
        return testStep;
    }

}