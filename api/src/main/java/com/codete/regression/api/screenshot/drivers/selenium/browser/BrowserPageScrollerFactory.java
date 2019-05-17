package com.codete.regression.api.screenshot.drivers.selenium.browser;

import com.codete.regression.api.screenshot.drivers.selenium.PageScroller;
import com.codete.regression.api.screenshot.drivers.selenium.PageScrollerFactory;
import org.openqa.selenium.WebDriver;

public class BrowserPageScrollerFactory implements PageScrollerFactory {

    @Override
    public PageScroller createPageScroller(WebDriver driver) {
        return new BrowserPageScroller(driver);
    }
}
