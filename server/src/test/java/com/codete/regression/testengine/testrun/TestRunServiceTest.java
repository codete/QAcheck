package com.codete.regression.testengine.testrun;

import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsRepository;
import com.codete.regression.testengine.testcase.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestRunServiceTest {

    private final ScreenshotStorage screenshotStorage = mock(ScreenshotStorage.class);
    private final TestRunRepository testRunRepository = mock(TestRunRepository.class);
    private final ComparisonSettingsRepository comparisonSettingsRepository = mock(ComparisonSettingsRepository.class);

    @InjectMocks
    private TestRunService testRunService;

    @Test
    void shouldDeleteNotNeededComparisonSettingsAndScreenshotsWhenDeletingTestRun() {

        String testRunUuid = "uuid";
        String testCaseUuid = "caseUuid";
        Long settingsId1 = 1L;
        Long settingsId2 = 5L;
        String screenshot1 = "part_1.png";
        String screenshot2 = "part_2.png";

        TestRun testRun = mock(TestRun.class);
        TestCase testCase = mock(TestCase.class);
        ComparisonSettings orphanSettings1 = mock(ComparisonSettings.class);
        ComparisonSettings orphanSettings2 = mock(ComparisonSettings.class);
        when(testRun.getUuid()).thenReturn(testRunUuid);
        when(testRun.getScreenshotPaths()).thenReturn(Set.of(screenshot1, screenshot2));
        when(testRun.getTestCase()).thenReturn(testCase);
        when(testCase.getUuid()).thenReturn(testCaseUuid);
        when(testCase.getLatestTestRun()).thenReturn(testRun);
        when(orphanSettings1.getId()).thenReturn(settingsId1);
        when(orphanSettings2.getId()).thenReturn(settingsId2);
        when(testRunRepository.findByUuid(testRunUuid)).thenReturn(Optional.of(testRun));
        when(testRunRepository.findTop1ByTestCaseUuidAndUuidNotOrderByRunTimestampDesc(testCaseUuid, testRunUuid))
                .thenReturn(Optional.empty());
        when(comparisonSettingsRepository.findAllOrphans()).thenReturn(Stream.of(orphanSettings1, orphanSettings2));
        when(comparisonSettingsRepository.countByBaselineScreenshotPath(screenshot1)).thenReturn(0L);
        when(comparisonSettingsRepository.countByBaselineScreenshotPath(screenshot2)).thenReturn(1L);

        testRunService.deleteTestRun(testRunUuid);

        verify(testRunRepository, times(1)).delete(testRun);
        verify(comparisonSettingsRepository, times(1)).deleteById(settingsId1);
        verify(comparisonSettingsRepository, times(1)).deleteById(settingsId2);
        verify(comparisonSettingsRepository, times(1)).countByBaselineScreenshotPath(screenshot1);
        verify(comparisonSettingsRepository, times(1)).countByBaselineScreenshotPath(screenshot2);
        verify(screenshotStorage, times(1)).deleteScreenshots(screenshot1);
        verify(screenshotStorage, times(0)).deleteScreenshots(screenshot2);
        verify(testRun, times(1)).getScreenshotPaths();
        verify(testRunRepository, times(1)).findTop1ByTestCaseUuidAndUuidNotOrderByRunTimestampDesc(testCaseUuid, testRunUuid);
        verify(testCase, times(1)).setLatestTestRun(null);
    }
}
