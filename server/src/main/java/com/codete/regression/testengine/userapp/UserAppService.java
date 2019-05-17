package com.codete.regression.testengine.userapp;

import com.codete.regression.authentication.user.User;
import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserAppRepository userAppRepository;
    private final ComparisonSettingsRepository comparisonSettingsRepository;
    private final ScreenshotStorage screenshotStorage;

    @Transactional(readOnly = true)
    public UserApp findUserAppOrCreateNewOne(User user, String appName) {
        return userAppRepository.findByUserUsernameAndAppName(user.getUsername(), appName)
                .orElseGet(() -> userAppRepository.save(new UserApp(appName, user)));
    }

    @Transactional(readOnly = true)
    public List<UserApp> findAllByUserUsername(String username) {
        return userAppRepository.findAllByUserUsername(username);
    }

    @Transactional
    public boolean doesUserHaveAccess(Long id, String username) {
        return userAppRepository.findById(id)
                .map(userApp -> userApp.getUsername().equals(username))
                .orElse(false);
    }

    @Transactional
    public void deleteUserApp(Long id) {
        userAppRepository.findById(id)
                .ifPresent(userApp -> {
                    String screenshotsFolder = screenshotStorage.getUserAppStorageLocation(userApp);
                    userAppRepository.delete(userApp);
                    comparisonSettingsRepository.findAllOrphans()
                            .map(ComparisonSettings::getId)
                            .forEach(comparisonSettingsRepository::deleteById);
                    screenshotStorage.deleteScreenshots(screenshotsFolder);
                });
    }

    @Transactional(readOnly = true)
    public Optional<UserApp> findById(Long id) {
        return userAppRepository.findById(id);
    }
}
