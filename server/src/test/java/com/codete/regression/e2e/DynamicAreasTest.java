package com.codete.regression.e2e;

import com.codete.regression.api.screenshot.ScreenshotComparator;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.config.TestApplicationConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class DynamicAreasTest extends TestApplicationConfig {

    private static final String APP_NAME = "e2e_test";
    private static final String TEST_NAME = DynamicAreasTest.class.getName();
    private static WebDriver driver;
    private ScreenshotComparator screenshotComparator;
    private boolean baselineInitialized;

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
        File page = new File(getClass().getClassLoader().getResource("pages/dynamicAreas.html").toURI());
        screenshotComparator = new ScreenshotComparator(serverUrl, API_KEY, APP_NAME, ScreenshotTakerType.BROWSER);
        driver.get("file:///" + page.getAbsolutePath());
        if (!baselineInitialized) {
            initializeDynamicAreas();
        }
    }

    @Test
    void shouldDoNotSeeDifferenceWhenDynamicElementsAreIgnored() {
        screenshotComparator.setAllowedDifferencePercentage(1);
        boolean screenshotEquals = screenshotComparator.compareScreenshotWithBaseline(driver, TEST_NAME);
        assertThat("Actual screenshot should match the baseline", screenshotEquals, is(true));
    }

    private void initializeDynamicAreas() {
        boolean screenshotEquals = screenshotComparator.ignoreDynamicElementsAndCompareWithBaseline(driver, TEST_NAME);
        assertThat("Baseline should be created", screenshotEquals, is(true));
        baselineInitialized = true;
    }

}
