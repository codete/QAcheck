package com.codete.regression.api.testgenerator.engine.mouseclick;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.screenshot.IgnoreAreaDto;
import com.codete.regression.api.testgenerator.TestCaseGeneratorRequest;
import com.codete.regression.api.testgenerator.engine.ActionFactory;
import com.codete.regression.api.testgenerator.engine.DynamicElementDetectionResult;
import com.codete.regression.api.testgenerator.engine.DynamicElementDetector;
import com.codete.regression.api.testgenerator.engine.SeleniumTestGenerator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

import static com.codete.regression.api.testgenerator.engine.SeleniumTestInstructions.getClickAction;

@Slf4j
public class ClickActionTestGenerator {

    private static final double DIFFERENCE_THRESHOLD = 0.0;
    private static final String DIRECTORY_POSTFIX = "click";
    private final DynamicElementDetector dynamicElementDetector;
    private final ActionFactory actionFactory = new ClickActionFactory();
    private final ClickElementDetector clickElementDetector = new ClickElementDetector();

    public ClickActionTestGenerator(ServerConnection serverConnection) {
        this.dynamicElementDetector = new DynamicElementDetector(serverConnection);
    }

    public void generateTests(RemoteWebDriver driver, List<IgnoreAreaDto> ignoreAreas,
                              TestCaseGeneratorRequest testCaseGeneratorRequest) {
        log.info("Searching for click elements that can change page state.");
        List<String> clickElements = clickElementDetector.searchElements(driver);
        log.info("{} elements will be analyzed.", clickElements.size());
        generateTests(driver, ignoreAreas, clickElements, testCaseGeneratorRequest);
    }

    private void generateTests(RemoteWebDriver driver, List<IgnoreAreaDto> ignoreAreas,
                               List<String> elements,
                               TestCaseGeneratorRequest testCaseGeneratorRequest) {
        SeleniumTestGenerator seleniumTestGenerator = new SeleniumTestGenerator(DIRECTORY_POSTFIX, testCaseGeneratorRequest);
        String baseUrl = driver.getCurrentUrl();
        int counter = 1;
        for (String elementXPath : elements) {
            try {
                WebElement webElement = driver.findElementByXPath(elementXPath);
                DynamicElementDetectionResult elementDetectionResult = dynamicElementDetector.
                        checkIfActionCausesPageChanges(driver, webElement, actionFactory, ignoreAreas);
                boolean actionOccurred = elementDetectionResult.getDifference() > DIFFERENCE_THRESHOLD;
                if (actionOccurred) {
                    seleniumTestGenerator.generateTest(getClickAction(elementXPath), elementDetectionResult, counter);
                    counter++;
                }
            } catch (Exception e) {
                log.warn("Exception during test generation for webElement. It will be omitted.");
            }
            driver.get(baseUrl);
        }
        log.info("Number of generated tests: {}", counter - 1);
    }
}
