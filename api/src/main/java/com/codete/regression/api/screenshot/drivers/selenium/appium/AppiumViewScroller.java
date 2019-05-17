package com.codete.regression.api.screenshot.drivers.selenium.appium;

import com.codete.regression.api.screenshot.drivers.selenium.PageScroller;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;

import java.time.Duration;

class AppiumViewScroller implements PageScroller {

    private static final double TOP_POINT_SCREEN_PERCENTAGE = 0.9;
    private static final double BOTTOM_POINT_SCREEN_PERCENTAGE = 0.2;
    private static final double SWIPE_ANCHOR_PERCENTAGE = 0.5;
    private static final int SWIPE_DURATION = 1000;
    private final AppiumDriver appiumDriver;
    private final Dimension dimension;
    private String previousPageSource;

    AppiumViewScroller(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
        this.dimension = appiumDriver.manage().window().getSize();
    }

    @Override
    public boolean wholePageVisited() {
        return previousPageSource != null && previousPageSource.equals(appiumDriver.getPageSource());
    }

    @Override
    public void scrollDown() {
        this.previousPageSource = appiumDriver.getPageSource();
        TouchAction swipeVertical = createSwipeVerticalAction(dimension, TOP_POINT_SCREEN_PERCENTAGE,
                BOTTOM_POINT_SCREEN_PERCENTAGE);
        swipeVertical.perform();
    }

    @Override
    public void scrollToTheTop() {
        String previousPageSource;
        do {
            previousPageSource = appiumDriver.getPageSource();
            TouchAction swipeVertical = createSwipeVerticalAction(dimension, BOTTOM_POINT_SCREEN_PERCENTAGE,
                    TOP_POINT_SCREEN_PERCENTAGE);
            swipeVertical.perform();
        } while (!previousPageSource.equals(appiumDriver.getPageSource()));
    }

    private TouchAction createSwipeVerticalAction(Dimension dimension, double swipeStartPercentage,
                                                  double startEndPercentage) {
        int anchor = (int) (dimension.width * SWIPE_ANCHOR_PERCENTAGE);
        int startPoint = (int) (dimension.height * swipeStartPercentage);
        int endPoint = (int) (dimension.height * startEndPercentage);
        return new TouchAction(appiumDriver)
                .press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(SWIPE_DURATION)))
                .moveTo(PointOption.point(anchor, endPoint))
                .release();
    }
}
