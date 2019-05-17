package com.codete.regression.examples.appium;

import com.codete.regression.api.screenshot.ScreenshotComparator;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.examples.AccountConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

//Android calculator application test which uses appium api
class AppiumCalculatorTest {

    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723/wd/hub";
    private static final String APP_PACKAGE = "com.android.calculator2";
    private static AppiumDriver<MobileElement> appiumDriver = null;
    private static WebElement calculatorResult = null;
    private ScreenshotComparator screenshotComparator = new ScreenshotComparator(AccountConfig.SERVER_URL,
            AccountConfig.API_KEY, "appium", ScreenshotTakerType.APPIUM);

    @BeforeAll
    static void setup() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", APP_PACKAGE);
        capabilities.setCapability("appActivity", APP_PACKAGE + ".Calculator");
        capabilities.setCapability("deviceName", "Nexus5X");
        appiumDriver = new AndroidDriver<>(new URL(APPIUM_SERVER_URL), capabilities);
        appiumDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        calculatorResult = appiumDriver.findElementById(APP_PACKAGE + ":id/result");
        Assertions.assertNotNull(calculatorResult);
    }

    @AfterEach
    void clear() {
        if (appiumDriver != null) {
            appiumDriver.findElementById("com.android.calculator2:id/clr").click();
        }
    }

    @AfterAll
    static void tearDown() {
        calculatorResult = null;
        if (appiumDriver != null) {
            appiumDriver.quit();
        }
        appiumDriver = null;
    }

    @Test
    void addition() {
        appiumDriver.findElementById(APP_PACKAGE + ":id/digit_1").click();
        appiumDriver.findElementById(APP_PACKAGE + ":id/op_add").click();
        appiumDriver.findElementById(APP_PACKAGE + ":id/digit_7").click();
        appiumDriver.findElementById(APP_PACKAGE + ":id/eq").click();
        WebDriverWait wait = new WebDriverWait(appiumDriver, 2);
        wait.until(ExpectedConditions.visibilityOf(appiumDriver.findElementById("com.android.calculator2:id/clr")));
        boolean screenshotEquals = screenshotComparator.compareScreenshotWithBaseline(appiumDriver, "calculator");
        Assertions.assertTrue(screenshotEquals, "Actual screenshot should match the baseline");
        String calculationResult = calculatorResult.getText().replace("Display is", "").trim();
        Assertions.assertEquals("8", calculationResult);
    }

}