package com.codete.regression.api.screenshot.drivers.selenium.appium;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.screenshot.ScreenshotTakerConfig;
import com.codete.regression.api.screenshot.drivers.ScreenshotTaker;
import com.codete.regression.api.screenshot.drivers.selenium.PageScroller;
import com.codete.regression.api.screenshot.drivers.selenium.SeleniumScreenshotTaker;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

import java.util.Collections;
import java.util.List;

public class AppiumScreenshotTaker implements ScreenshotTaker {

    private final SeleniumScreenshotTaker seleniumScreenshotTaker = new SeleniumScreenshotTaker();

    @Override
    public List<Screenshot> takeScreenshots(WebDriver driver, ScreenshotTakerConfig screenshotTakerConfig) {
        List<Screenshot> screenshots;
        if (screenshotTakerConfig.isFullPageScreenshot()) {
            PageScroller pageScroller = new AppiumViewScroller((AppiumDriver) driver);
            screenshots = seleniumScreenshotTaker.takeFullPageScreenshot(driver, pageScroller);
        } else {
            screenshots = Collections.singletonList(seleniumScreenshotTaker.takeScreenshot(driver));
        }
        return screenshots;
    }
}
