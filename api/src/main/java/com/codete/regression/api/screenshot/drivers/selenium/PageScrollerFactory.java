package com.codete.regression.api.screenshot.drivers.selenium;

import org.openqa.selenium.WebDriver;

public interface PageScrollerFactory {
    PageScroller createPageScroller(WebDriver driver);
}
