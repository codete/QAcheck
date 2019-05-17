package com.codete.regression.api.screenshot.drivers.selenium.appium;

import com.codete.regression.api.screenshot.drivers.selenium.PageScroller;
import com.codete.regression.api.screenshot.drivers.selenium.PageScrollerFactory;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

public class AppiumPageScrollerFactory implements PageScrollerFactory {

    @Override
    public PageScroller createPageScroller(WebDriver driver) {
        return new AppiumViewScroller((AppiumDriver) driver);
    }
}
