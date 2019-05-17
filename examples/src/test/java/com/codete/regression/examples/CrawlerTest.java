package com.codete.regression.examples;

import com.codete.regression.api.crawler.SitemapCrawler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

//Example test which uses crawler api - runs crawler through site map file and takes screenshots of each page
class CrawlerTest {

    private WebDriver driver;

    @Test
    void crawlerLocalEnvironmentTest() {
        driver = new ChromeDriver();
        runCrawler(driver);
    }

    @Test
    void crawlerBrowserStackEnvironmentTest() throws MalformedURLException {
        driver = BrowserStackRemoteDriver.crateBrowserStackWebDriver();
        runCrawler(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void runCrawler(WebDriver driver) {
        ClassLoader classLoader = getClass().getClassLoader();
        String sitemapFileName = "example_sitemap.xml";
        File sitemapFile = new File(classLoader.getResource(sitemapFileName).getFile());
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        SitemapCrawler sitemapCrawler = new SitemapCrawler(AccountConfig.SERVER_URL,
                AccountConfig.API_KEY, sitemapFileName, false);
        boolean result = sitemapCrawler.crawlThroughSitemap(driver, sitemapFile);
        Assertions.assertTrue(result, "Actual screenshot should match the baseline");
    }
}
