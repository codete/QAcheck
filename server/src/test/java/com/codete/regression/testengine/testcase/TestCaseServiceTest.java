package com.codete.regression.testengine.testcase;

import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TestCaseServiceTest {

    private final TestCaseRepository testCaseRepository = mock(TestCaseRepository.class);
    private final ComparisonSettingsRepository comparisonSettingsRepository = mock(ComparisonSettingsRepository.class);
    private final ScreenshotStorage screenshotStorage = mock(ScreenshotStorage.class);

    @InjectMocks
    private TestCaseService testCaseService;

    @Test
    void shouldDeleteNotNeededComparisonSettingsAndScreenshotsWhenDeletingTestCase() {

        String testCaseUuid = "uuid";
        Long settingsId1 = 1L;
        Long settingsId2 = 5L;
        String screenshotsFolder = "folder";

        TestCase testCase = mock(TestCase.class);
        ComparisonSettings orphanSettings1 = mock(ComparisonSettings.class);
        ComparisonSettings orphanSettings2 = mock(ComparisonSettings.class);
        when(orphanSettings1.getId()).thenReturn(settingsId1);
        when(orphanSettings2.getId()).thenReturn(settingsId2);
        when(testCaseRepository.findByUuid(testCaseUuid)).thenReturn(Optional.of(testCase));
        when(comparisonSettingsRepository.findAllOrphans()).thenReturn(Stream.of(orphanSettings1, orphanSettings2));
        when(screenshotStorage.getTestCaseStorageLocation(testCase)).thenReturn(screenshotsFolder);

        testCaseService.deleteTestCase(testCaseUuid);

        verify(testCaseRepository, times(1)).delete(testCase);
        verify(comparisonSettingsRepository, times(1)).deleteById(settingsId1);
        verify(comparisonSettingsRepository, times(1)).deleteById(settingsId2);
        verify(screenshotStorage, times(1)).deleteScreenshots(screenshotsFolder);

    }
}
