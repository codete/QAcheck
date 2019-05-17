package com.codete.regression.examples.browser;

import com.codete.regression.api.screenshot.ScreenshotComparator;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.examples.AccountConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

//Selenium browser test of static page
class BrowserStaticPageTest {

    private ScreenshotComparator screenshotComparator = new ScreenshotComparator(AccountConfig.SERVER_URL,
            AccountConfig.API_KEY, "languageinternational", ScreenshotTakerType.BROWSER);
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        Dimension dimension = new Dimension(1200, 900);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().setSize(dimension);
        screenshotComparator.setFullPageScreenshot(true);
    }

    @AfterEach
    void tearDown() {
        driver.close();
    }

    @Test
    void singlePageTest() {
        driver.get("https://www.languageinternational.com/our-advisors");
        boolean screenshotEquals = screenshotComparator.compareScreenshotWithBaseline(driver, "advisors");
        Assertions.assertTrue(screenshotEquals, "Actual screenshot should match the baseline");
    }

}
