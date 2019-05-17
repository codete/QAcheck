package com.codete.regression.api.screenshot.drivers.selenium;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.screenshot.ScreenshotTakerConfig;
import com.codete.regression.api.screenshot.drivers.ScreenshotTaker;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MultipleScreenshotsTaker implements ScreenshotTaker {

    private static final int TIME_TO_WAIT_BEFORE_NEXT_SCREENSHOT_MS = 1000;
    private static final int NUMBER_OF_SCREENSHOTS = 6;
    private final ScreenshotTaker screenshotTaker;
    private final PageScrollerFactory pageScrollerFactory;

    public MultipleScreenshotsTaker(ScreenshotTaker screenshotTaker, PageScrollerFactory pageScrollerFactory) {
        this.screenshotTaker = screenshotTaker;
        this.pageScrollerFactory = pageScrollerFactory;
    }

    @Override
    public List<Screenshot> takeScreenshots(WebDriver driver, ScreenshotTakerConfig screenshotTakerConfig) {
        PageScroller pageScroller = pageScrollerFactory.createPageScroller(driver);
        List<Screenshot> screenshots = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SCREENSHOTS; i++) {
            if (screenshotTakerConfig.isFullPageScreenshot()) {
                pageScroller.scrollToTheTop();
            } else {
                try {
                    Thread.sleep(TIME_TO_WAIT_BEFORE_NEXT_SCREENSHOT_MS);
                } catch (InterruptedException e) {
                    log.warn("InterruptedException during waiting to take next screenshot.");
                }
            }
            screenshots.addAll(screenshotTaker.takeScreenshots(driver, screenshotTakerConfig));
        }
        return screenshots;
    }
}
