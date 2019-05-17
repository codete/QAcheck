package com.codete.regression.examples.browser;

import com.codete.regression.api.screenshot.ScreenshotComparator;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.examples.AccountConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

//Selenium browser test of static page
class BrowserDynamicPageTest {

    private static final String DYNAMIC_PAGE_URL = "https://codete.com/";
    private final ScreenshotComparator screenshotComparator = new ScreenshotComparator(AccountConfig.SERVER_URL,
            AccountConfig.API_KEY, "codete", ScreenshotTakerType.BROWSER);
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(DYNAMIC_PAGE_URL);
        screenshotComparator.setFullPageScreenshot(true);
    }

    @AfterEach
    void tearDown() {
        driver.close();
    }

    @Test
    void removeDynamicElementsAndTakeScreenshot() {
        CodeteDynamicElementsFilter codeteDynamicElementsFilter = new CodeteDynamicElementsFilter();
        codeteDynamicElementsFilter.deleteAllDynamicElementsOnPage(driver);
        boolean screenshotEquals = screenshotComparator.compareScreenshotWithBaseline(driver,
                "landing_page_without_dynamic_elements");
        Assertions.assertTrue(screenshotEquals, "Actual screenshot should match the baseline");
    }

    @Test
    void initializeIgnoreAreas() {
        boolean screenshotEquals = screenshotComparator.ignoreDynamicElementsAndCompareWithBaseline(driver,
                "landing_page_init_ignore_areas");
        Assertions.assertTrue(screenshotEquals, "Actual screenshot should match the baseline");
    }
}
