package com.codete.regression.api.testgenerator.engine.singlepagepath;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.screenshot.IgnoreAreaDto;
import com.codete.regression.api.testgenerator.TestCaseGeneratorRequest;
import com.codete.regression.api.testgenerator.engine.ActionFactory;
import com.codete.regression.api.testgenerator.engine.DynamicElementDetectionResult;
import com.codete.regression.api.testgenerator.engine.DynamicElementDetector;
import com.codete.regression.api.testgenerator.engine.SeleniumTestGenerator;
import com.codete.regression.api.testgenerator.engine.mouseclick.ClickActionFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

import static com.codete.regression.api.testgenerator.engine.SeleniumTestInstructions.getGoThroughPathAction;

@Slf4j
public class SinglePagePathTestGenerator {

    private static final double DIFFERENCE_THRESHOLD = 0.0;
    private static final String DIRECTORY_POSTFIX = "modal";
    private final DynamicElementDetector dynamicElementDetector;
    private final ActionFactory actionFactory = new ClickActionFactory();
    private final SinglePagePathDetector singlePagePathDetector = new SinglePagePathDetector();

    public SinglePagePathTestGenerator(ServerConnection serverConnection) {
        this.dynamicElementDetector = new DynamicElementDetector(serverConnection);
    }

    public void generateTests(RemoteWebDriver driver, List<IgnoreAreaDto> ignoreAreas,
                              TestCaseGeneratorRequest testCaseGeneratorRequest) {
        log.info("Searching for single page paths.");
        List<SinglePagePath> singlePagePaths = singlePagePathDetector.findSinglePagePaths(driver);
        log.info("{} paths will be analyzed.", singlePagePaths.size());
        generateTests(driver, ignoreAreas, singlePagePaths, testCaseGeneratorRequest);
    }

    private void generateTests(RemoteWebDriver driver, List<IgnoreAreaDto> ignoreAreas,
                               List<SinglePagePath> singlePagePaths,
                               TestCaseGeneratorRequest testCaseGeneratorRequest) {
        SeleniumTestGenerator seleniumTestGenerator = new SeleniumTestGenerator(DIRECTORY_POSTFIX, testCaseGeneratorRequest);
        String baseUrl = driver.getCurrentUrl();
        int counter = 1;
        for (SinglePagePath singlePagePath : singlePagePaths) {
            try {
                goThroughPath(driver, singlePagePath);
                WebElement webElement = driver.findElementByXPath(singlePagePath.getClickElementsPath().getLast());
                DynamicElementDetectionResult elementDetectionResult = dynamicElementDetector.
                        checkIfActionCausesPageChanges(driver, webElement, actionFactory, ignoreAreas);
                boolean actionOccurred = elementDetectionResult.getDifference() > DIFFERENCE_THRESHOLD;
                if (actionOccurred) {
                    seleniumTestGenerator.generateTest(getGoThroughPathAction(singlePagePath.getClickElementsPath()),
                            elementDetectionResult, counter);
                    counter++;
                }
            } catch (Exception e) {
                log.warn("Exception during test generation for webElement. It will be omitted.");
            }
            driver.get(baseUrl);
        }
        log.info("Number of generated tests: {}", counter - 1);
    }

    private void goThroughPath(RemoteWebDriver driver, SinglePagePath singlePagePath) {
        for (int i = 0; i < singlePagePath.getClickElementsPath().size() - 1; i++) {
            WebElement webElement = driver.findElementByXPath(singlePagePath.getClickElementsPath().get(i));
            actionFactory.performInitAction(driver, webElement);
            actionFactory.performAction(driver, webElement);
        }
    }
}
