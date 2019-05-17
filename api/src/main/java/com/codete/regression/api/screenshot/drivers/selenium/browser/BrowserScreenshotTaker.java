package com.codete.regression.api.screenshot.drivers.selenium.browser;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.screenshot.ScreenshotTakerConfig;
import com.codete.regression.api.screenshot.drivers.ScreenshotTaker;
import com.codete.regression.api.screenshot.drivers.selenium.PageScroller;
import com.codete.regression.api.screenshot.drivers.selenium.SeleniumScreenshotTaker;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Collections;
import java.util.List;

@Slf4j
public class BrowserScreenshotTaker implements ScreenshotTaker {

    private final SeleniumScreenshotTaker seleniumScreenshotTaker = new SeleniumScreenshotTaker();

    @Override
    public List<Screenshot> takeScreenshots(WebDriver driver, ScreenshotTakerConfig screenshotTakerConfig) {
        log.info("Taking screenshot of page {}.", driver.getCurrentUrl());
        List<Screenshot> screenshots;
        if (screenshotTakerConfig.isFullPageScreenshot()) {
            PageScroller pageScroller = new BrowserPageScroller(driver);
            screenshots = seleniumScreenshotTaker.takeFullPageScreenshot(driver, pageScroller);
        } else {
            screenshots = Collections.singletonList(seleniumScreenshotTaker.takeScreenshot(driver));
        }
        return screenshots;
    }

}
