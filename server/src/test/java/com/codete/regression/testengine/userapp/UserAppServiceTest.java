package com.codete.regression.testengine.userapp;

import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserAppServiceTest {

    private final UserAppRepository userAppRepository = mock(UserAppRepository.class);
    private final ComparisonSettingsRepository comparisonSettingsRepository = mock(ComparisonSettingsRepository.class);
    private final ScreenshotStorage screenshotStorage = mock(ScreenshotStorage.class);

    @InjectMocks
    private UserAppService userAppService;

    @Test
    void shouldDeleteNotNeededComparisonSettingsAndScreenshotsWhenDeletingUserApp() {

        Long userAppId = 15L;
        Long settingsId1 = 1L;
        Long settingsId2 = 5L;
        String screenshotsFolder = "folder";

        UserApp userApp = mock(UserApp.class);
        ComparisonSettings orphanSettings1 = mock(ComparisonSettings.class);
        ComparisonSettings orphanSettings2 = mock(ComparisonSettings.class);
        when(orphanSettings1.getId()).thenReturn(settingsId1);
        when(orphanSettings2.getId()).thenReturn(settingsId2);
        when(userAppRepository.findById(userAppId)).thenReturn(Optional.of(userApp));
        when(comparisonSettingsRepository.findAllOrphans()).thenReturn(Stream.of(orphanSettings1, orphanSettings2));
        when(screenshotStorage.getUserAppStorageLocation(userApp)).thenReturn(screenshotsFolder);

        userAppService.deleteUserApp(userAppId);

        verify(userAppRepository, times(1)).delete(userApp);
        verify(comparisonSettingsRepository, times(1)).deleteById(settingsId1);
        verify(comparisonSettingsRepository, times(1)).deleteById(settingsId2);
        verify(screenshotStorage, times(1)).deleteScreenshots(screenshotsFolder);

    }

}
