package com.codete.regression.api.screenshot.drivers;

import com.codete.regression.api.screenshot.drivers.selenium.MultipleScreenshotsTaker;
import com.codete.regression.api.screenshot.drivers.selenium.appium.AppiumPageScrollerFactory;
import com.codete.regression.api.screenshot.drivers.selenium.appium.AppiumScreenshotTaker;
import com.codete.regression.api.screenshot.drivers.selenium.browser.BrowserPageScrollerFactory;
import com.codete.regression.api.screenshot.drivers.selenium.browser.BrowserScreenshotTaker;

public enum ScreenshotTakerType {

    BROWSER {
        @Override
        public ScreenshotTaker getScreenshotTaker() {
            return new BrowserScreenshotTaker();
        }

        @Override
        public ScreenshotTaker getMultipleScreenshotsTaker() {
            return new MultipleScreenshotsTaker(BROWSER.getScreenshotTaker(), new BrowserPageScrollerFactory());
        }
    }, APPIUM {
        @Override
        public ScreenshotTaker getScreenshotTaker() {
            return new AppiumScreenshotTaker();
        }

        @Override
        public ScreenshotTaker getMultipleScreenshotsTaker() {
            return new MultipleScreenshotsTaker(APPIUM.getScreenshotTaker(), new AppiumPageScrollerFactory());
        }
    };

    public abstract ScreenshotTaker getScreenshotTaker();

    public abstract ScreenshotTaker getMultipleScreenshotsTaker();

}
