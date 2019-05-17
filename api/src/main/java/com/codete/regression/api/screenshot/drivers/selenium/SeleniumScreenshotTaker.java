package com.codete.regression.api.screenshot.drivers.selenium;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.screenshot.drivers.ScreenshotTaker;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;

public class SeleniumScreenshotTaker {

    public Screenshot takeScreenshot(WebDriver driver) {
        byte[] scrFile = ((RemoteWebDriver) driver).getScreenshotAs(OutputType.BYTES);
        String screenshotStepName = ScreenshotTaker.SCREENSHOT_PART_PREFIX + 1;
        return new Screenshot(screenshotStepName, scrFile);
    }

    public List<Screenshot> takeFullPageScreenshot(WebDriver driver, PageScroller pageScroller) {
        List<Screenshot> screenshotParts = new ArrayList<>();
        int screenshotPartCounter = 1;
        while (!pageScroller.wholePageVisited()) {
            byte[] scrFile = ((RemoteWebDriver) driver).getScreenshotAs(OutputType.BYTES);
            String screenshotStepName = ScreenshotTaker.SCREENSHOT_PART_PREFIX + screenshotPartCounter;
            screenshotParts.add(new Screenshot(screenshotStepName, scrFile));
            pageScroller.scrollDown();
            screenshotPartCounter++;
        }
        return screenshotParts;
    }

}
