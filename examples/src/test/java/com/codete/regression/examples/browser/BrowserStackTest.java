package com.codete.regression.examples.browser;

import com.codete.regression.api.screenshot.ScreenshotComparator;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.examples.AccountConfig;
import com.codete.regression.examples.BrowserStackRemoteDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

class BrowserStackTest {

    private ScreenshotComparator screenshotComparator = new ScreenshotComparator(AccountConfig.SERVER_URL,
            AccountConfig.API_KEY, "languageinternational", ScreenshotTakerType.BROWSER);
    private WebDriver driver;

    @BeforeEach
    void setUp() throws MalformedURLException {
        driver = BrowserStackRemoteDriver.crateBrowserStackWebDriver();
        screenshotComparator.setFullPageScreenshot(true);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void singlePageTest() {
        driver.get("https://www.languageinternational.com/our-advisors");
        boolean screenshotEquals = screenshotComparator.compareScreenshotWithBaseline(driver, "advisors");
        Assertions.assertTrue(screenshotEquals, "Actual screenshot should match the baseline");
    }

}
