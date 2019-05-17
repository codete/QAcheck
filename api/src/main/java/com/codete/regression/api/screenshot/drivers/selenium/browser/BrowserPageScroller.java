package com.codete.regression.api.screenshot.drivers.selenium.browser;

import com.codete.regression.api.screenshot.drivers.selenium.PageScroller;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

class BrowserPageScroller implements PageScroller {

    private static final String SCROLL_TO_JS_INSTRUCTION = "window.scrollTo(%d,%d)";
    private final JavascriptExecutor jsx;
    private long pageHeight;
    private long windowHeight;
    private long currentHeightOffset;

    BrowserPageScroller(WebDriver driver) {
        this.jsx = (JavascriptExecutor) driver;
        this.pageHeight = getPageHeight();
        this.windowHeight = getWindowHeight();
        this.currentHeightOffset = 0L;
    }

    @Override
    public boolean wholePageVisited() {
        return currentHeightOffset >= pageHeight;
    }

    @Override
    public void scrollDown() {
        currentHeightOffset += windowHeight;
        scrollTo(0, currentHeightOffset);
    }

    @Override
    public void scrollToTheTop() {
        scrollTo(0, 0);
    }

    private long getPageHeight() {
        return (long) jsx.executeScript("return document.body.scrollHeight");
    }

    private long getWindowHeight() {
        return (long) jsx.executeScript("return window.top.innerHeight");
    }

    private void scrollTo(long x, long y) {
        jsx.executeScript(String.format(SCROLL_TO_JS_INSTRUCTION, x, y));
    }
}
