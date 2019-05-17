package com.codete.regression.api.testgenerator.engine.singlepagepath;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class SinglePagePathDetectorITTest {

    private final SinglePagePathDetector singlePagePathDetector = new SinglePagePathDetector();
    private static RemoteWebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.phantomjs().setup();
        driver = new PhantomJSDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.close();
    }

    @BeforeEach
    void setUp() throws URISyntaxException {
        File page = new File(getClass().getClassLoader().getResource("singlePageForm.html").toURI());
        driver.get("file:///" + page.getAbsolutePath());
    }

    @Test
    void shouldGenerateTestScenarios() {
        List<SinglePagePath> pagePaths = singlePagePathDetector.findSinglePagePaths(driver);

        assertThat("Test scenario should be found", pagePaths, hasSize(3));
    }

}