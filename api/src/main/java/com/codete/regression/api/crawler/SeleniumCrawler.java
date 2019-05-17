package com.codete.regression.api.crawler;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.screenshot.ScreenshotComparator;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Set;

@Slf4j
public class SeleniumCrawler {

    private final ScreenshotComparator screenshotComparator;
    private final boolean detectAndIgnoreDynamicAreas;

    public SeleniumCrawler(String serverUrl, String apiKey, String appName, boolean detectAndIgnoreDynamicAreas) {
        String escapedAppName = replaceAllInvalidCharacters(appName);
        this.screenshotComparator = new ScreenshotComparator(serverUrl, apiKey, escapedAppName, ScreenshotTakerType.BROWSER);
        this.screenshotComparator.setFullPageScreenshot(true);
        this.detectAndIgnoreDynamicAreas = detectAndIgnoreDynamicAreas;
    }

    public boolean takeScreenshotAndCompareWithTheBaseline(WebDriver driver, String url) {
        driver.get(url);
        String testName = replaceAllInvalidCharacters(url);
        boolean comparisonResult;
        if (detectAndIgnoreDynamicAreas) {
            comparisonResult = screenshotComparator.ignoreDynamicElementsAndCompareWithBaseline(driver, testName);
        } else {
            comparisonResult = screenshotComparator.compareScreenshotWithBaseline(driver, testName);
        }
        printResult(url, comparisonResult);
        return comparisonResult;
    }

    boolean takeScreenshotsAndCompareWithTheBaseline(WebDriver driver, Set<String> urls) {
        boolean result = true;
        for (String url : urls) {
            boolean resultPart = takeScreenshotAndCompareWithTheBaseline(driver, url);
            result = result && resultPart;
        }
        return result;
    }

    private String replaceAllInvalidCharacters(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }

    private void printResult(String url, boolean comparisonResult) {
        if (comparisonResult) {
            log.info("Image comparison for URL={} passed.", url);
        } else {
            log.error("Image comparison for URL={} failed. Current screenshot is different than baseline.", url);
        }
    }

}
