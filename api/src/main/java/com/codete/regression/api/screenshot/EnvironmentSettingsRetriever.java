package com.codete.regression.api.screenshot;

import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

class EnvironmentSettingsRetriever {

    private static final String UNKNOWN_ENVIRONMENT_PROPERTY = "unknown";
    private static final String MOBILE_PLATFORM_NAME = "platformName";
    private static final String MOBILE_DEFAULT_BROWSER_NAME = "Mobile app";
    private final ScreenshotTakerType screenshotTakerType;

    EnvironmentSettingsRetriever(ScreenshotTakerType screenshotTakerType) {
        this.screenshotTakerType = screenshotTakerType;
    }

    EnvironmentSettings retrieveEnvironmentSettings(WebDriver driver) {
        EnvironmentSettings environmentSettings = new EnvironmentSettings();
        if (screenshotTakerType == ScreenshotTakerType.APPIUM && driver instanceof RemoteWebDriver) {
            Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
            //TODO maybe we should set app name instead of browser
            environmentSettings.setOs(cap.getCapability(MOBILE_PLATFORM_NAME).toString());
            environmentSettings.setBrowser(MOBILE_DEFAULT_BROWSER_NAME);
        } else if (driver instanceof RemoteWebDriver) {
            Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
            environmentSettings.setOs(cap.getPlatform().toString());
            environmentSettings.setBrowser(cap.getBrowserName().toLowerCase());
        } else {
            environmentSettings.setOs(UNKNOWN_ENVIRONMENT_PROPERTY);
            environmentSettings.setBrowser(UNKNOWN_ENVIRONMENT_PROPERTY);
        }
        Dimension dimension = driver.manage().window().getSize();
        environmentSettings.setViewPortHeight(dimension.getHeight());
        environmentSettings.setViewPortWidth(dimension.getWidth());
        return environmentSettings;
    }
}
