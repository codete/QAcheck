package com.codete.regression.api.testgenerator.engine;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.screenshot.IgnoreAreaDto;
import com.codete.regression.api.testgenerator.imagecomparator.ImageComparatorRequest;
import com.codete.regression.api.testgenerator.imagecomparator.ImageComparatorResponse;
import com.codete.regression.api.testgenerator.imagecomparator.ImageComparatorService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

@Slf4j
public class DynamicElementDetector {

    private static final String SCROLL_ELEMENT_INTO_MIDDLE = "var viewPortHeight = " +
            "Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
            + "var elementTop = arguments[0].getBoundingClientRect().top;"
            + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
    private final ImageComparatorService imageComparatorService;

    public DynamicElementDetector(ServerConnection serverConnection) {
        this.imageComparatorService = new ImageComparatorService(serverConnection);
    }

    public DynamicElementDetectionResult checkIfActionCausesPageChanges(RemoteWebDriver driver, WebElement webElement,
                                                                        ActionFactory actionFactory,
                                                                        List<IgnoreAreaDto> ignoreAreas) {
        ((JavascriptExecutor) driver).executeScript(SCROLL_ELEMENT_INTO_MIDDLE, webElement);
        actionFactory.performInitAction(driver, webElement);
        byte[] screenshotBefore = takeScreenshot(driver);
        actionFactory.performAction(driver, webElement);
        byte[] screenshotAfter = takeScreenshot(driver);
        long yOffset = (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
        ImageComparatorRequest imageComparatorRequest = new ImageComparatorRequest(ignoreAreas, yOffset, screenshotBefore,
                screenshotAfter);
        ImageComparatorResponse imageComparisonResult = imageComparatorService.compareImages(imageComparatorRequest);
        return new DynamicElementDetectionResult(screenshotBefore, screenshotAfter, imageComparisonResult);
    }

    private byte[] takeScreenshot(RemoteWebDriver driver) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.warn("Waiting interrupted.");
        }
        return driver.getScreenshotAs(OutputType.BYTES);
    }
}
