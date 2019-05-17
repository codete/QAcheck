package com.codete.regression.e2e;

import com.codete.regression.api.crawler.SitemapCrawler;
import com.codete.regression.config.TestApplicationConfig;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.testcase.TestCaseService;
import com.codete.regression.testengine.testcase.request.MultiTestCaseRequest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CrawlerTest extends TestApplicationConfig {

    private static final String APP_NAME = "crawler_e2e_test";
    private static WebDriver driver;
    private SitemapCrawler sitemapCrawler;
    private File sitemapFile;

    @Autowired
    private TestCaseService testCaseService;

    @LocalServerPort
    private int serverPort;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.phantomjs().setup();
        driver = new PhantomJSDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.close();
    }

    @BeforeEach
    void setUp() throws URISyntaxException {
        String serverUrl = "http://localhost:" + serverPort;
        sitemapFile = new File(getClass().getClassLoader().getResource("sitemap/sitemap.xml").toURI());
        sitemapCrawler = new SitemapCrawler(serverUrl, API_KEY, APP_NAME, false);
    }

    @Test
    void shouldCreateTestCaseForEachSitemapEntry() {
        boolean result = sitemapCrawler.crawlThroughSitemap(driver, sitemapFile);
        assertThat("Baseline should be created", result, is(true));
        MultiTestCaseRequest request = new MultiTestCaseRequest();
        request.setAppName(APP_NAME);
        request.setUsername(USERNAME);
        Page<TestCase> testCasesPage = testCaseService.getAllTestCases(request);
        assertThat("Test case for each sitemap entry should be created", testCasesPage.getTotalElements(), is(2L));
    }

}
