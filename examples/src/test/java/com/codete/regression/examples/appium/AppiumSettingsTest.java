package com.codete.regression.examples.appium;

import com.codete.regression.api.screenshot.ScreenshotComparator;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.examples.AccountConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

//Android settings test (settings application has vertical scroll) which uses appium api
class AppiumSettingsTest {

    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723/wd/hub";
    private static final String APP_PACKAGE = "com.android.settings";
    private static AppiumDriver<MobileElement> appiumDriver = null;
    private static ScreenshotComparator screenshotComparator = new ScreenshotComparator(AccountConfig.SERVER_URL,
            AccountConfig.API_KEY, "appium", ScreenshotTakerType.APPIUM);

    @BeforeAll
    static void setup() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", APP_PACKAGE);
        capabilities.setCapability("appActivity", APP_PACKAGE + ".Settings");
        capabilities.setCapability("deviceName", "Nexus5X");
        appiumDriver = new AndroidDriver<>(new URL(APPIUM_SERVER_URL), capabilities);
        appiumDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        screenshotComparator.setFullPageScreenshot(true);
    }

    @AfterAll
    static void tearDown() {
        if (appiumDriver != null) {
            appiumDriver.quit();
        }
    }

    @Test
    void screenshotComparator() {
        WebDriverWait wait = new WebDriverWait(appiumDriver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard_tile")));
        boolean screenshotEquals = screenshotComparator.compareScreenshotWithBaseline(appiumDriver, "settings");
        Assertions.assertTrue(screenshotEquals, "Actual screenshot should match the baseline");
    }
}
