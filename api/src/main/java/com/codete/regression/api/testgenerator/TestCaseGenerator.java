package com.codete.regression.api.testgenerator;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.crawler.JsoupCrawler;
import com.codete.regression.api.screenshot.IgnoreAreaDto;
import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.screenshot.ScreenshotTakerConfig;
import com.codete.regression.api.screenshot.drivers.ScreenshotTaker;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.api.testgenerator.engine.mouseclick.ClickActionTestGenerator;
import com.codete.regression.api.testgenerator.engine.mousehover.HoverActionTestGenerator;
import com.codete.regression.api.testgenerator.engine.singlepagepath.SinglePagePathTestGenerator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestCaseGenerator {

    private final DynamicAreasService dynamicAreasService;
    private final ClickActionTestGenerator clickActionTestGenerator;
    private final HoverActionTestGenerator hoverActionTestGenerator;
    private final SinglePagePathTestGenerator singlePagePathTestGenerator;
    private final String apiKey;
    private final JsoupCrawler jsoupCrawler = new JsoupCrawler();
    private final ScreenshotTaker screenshotTaker = ScreenshotTakerType.BROWSER.getMultipleScreenshotsTaker();

    public TestCaseGenerator(String serverUrl, String apiKey) {
        ServerConnection serverConnection = new ServerConnection(serverUrl, apiKey);
        this.apiKey = apiKey;
        this.dynamicAreasService = new DynamicAreasService(serverConnection);
        this.hoverActionTestGenerator = new HoverActionTestGenerator(serverConnection);
        this.clickActionTestGenerator = new ClickActionTestGenerator(serverConnection);
        this.singlePagePathTestGenerator = new SinglePagePathTestGenerator(serverConnection);
    }

    public void generateTestCases(TestCaseGeneratorRequest testCaseGeneratorRequest, RemoteWebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        testCaseGeneratorRequest.setApiKey(apiKey);
        Collection<String> pages = Collections.singletonList(testCaseGeneratorRequest.getPageUrl());
        if (testCaseGeneratorRequest.isRunWithCrawler()) {
            pages = jsoupCrawler.search(testCaseGeneratorRequest.getPageUrl(),
                    testCaseGeneratorRequest.getCrawlerMaxPagesToVisit());
        }
        pages.forEach(page -> {
            testCaseGeneratorRequest.setPageUrl(page);
            runTestGenerationForSinglePage(testCaseGeneratorRequest, driver);
        });
    }

    private void runTestGenerationForSinglePage(TestCaseGeneratorRequest testCaseGeneratorRequest,
                                                RemoteWebDriver driver) {
        log.info("Generating tests fo page {}.", testCaseGeneratorRequest.getPageUrl());
        driver.get(testCaseGeneratorRequest.getPageUrl());
        List<IgnoreAreaDto> ignoreAreas = detectIgnoreAreas(driver);
        hoverActionTestGenerator.generateTests(driver, ignoreAreas, testCaseGeneratorRequest);
        clickActionTestGenerator.generateTests(driver, ignoreAreas, testCaseGeneratorRequest);
        singlePagePathTestGenerator.generateTests(driver, ignoreAreas, testCaseGeneratorRequest);
        log.info("Tests generated.");
    }

    private List<IgnoreAreaDto> detectIgnoreAreas(RemoteWebDriver driver) {
        ScreenshotTakerConfig screenshotTakerConfig = new ScreenshotTakerConfig();
        screenshotTakerConfig.setFullPageScreenshot(true);
        List<Screenshot> screenshots = screenshotTaker.takeScreenshots(driver, screenshotTakerConfig);
        return dynamicAreasService.detectDynamicAreas(screenshots);
    }

}
