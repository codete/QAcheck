package com.codete.regression.api.testgenerator.engine.mousehover;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.screenshot.IgnoreAreaDto;
import com.codete.regression.api.testgenerator.TestCaseGeneratorRequest;
import com.codete.regression.api.testgenerator.engine.ActionFactory;
import com.codete.regression.api.testgenerator.engine.DynamicElementDetectionResult;
import com.codete.regression.api.testgenerator.engine.DynamicElementDetector;
import com.codete.regression.api.testgenerator.engine.SeleniumTestGenerator;
import com.codete.regression.api.testgenerator.engine.WebElementXPathRetriever;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.codete.regression.api.testgenerator.engine.SeleniumTestInstructions.getHoverAction;

@Slf4j
public class HoverActionTestGenerator {

    private static final String[] TAGS_TO_ANALYZE = {"a", "button"};
    private static final double DIFFERENCE_THRESHOLD = 0.0;
    private static final String DIRECTORY_POSTFIX = "hover";
    private final DynamicElementDetector dynamicElementDetector;
    private final ActionFactory actionFactory = new HoverActionFactory();
    private final WebElementXPathRetriever webElementXPathRetriever = new WebElementXPathRetriever();

    public HoverActionTestGenerator(ServerConnection serverConnection) {
        this.dynamicElementDetector = new DynamicElementDetector(serverConnection);
    }

    public void generateTests(RemoteWebDriver driver, List<IgnoreAreaDto> ignoreAreas,
                              TestCaseGeneratorRequest testCaseGeneratorRequest) {
        log.info("Searching for mouse hover elements that can change page state.");
        List<WebElement> elements = new ArrayList<>();
        Stream.of(TAGS_TO_ANALYZE).forEach(tag -> elements.addAll(driver.findElementsByTagName(tag)));
        log.info("{} elements will be analyzed.", elements.size());
        generateTests(driver, ignoreAreas, elements, testCaseGeneratorRequest);
    }

    private void generateTests(RemoteWebDriver driver, List<IgnoreAreaDto> ignoreAreas,
                               List<WebElement> elements,
                               TestCaseGeneratorRequest testCaseGeneratorRequest) {
        int counter = 1;
        SeleniumTestGenerator seleniumTestGenerator = new SeleniumTestGenerator(DIRECTORY_POSTFIX, testCaseGeneratorRequest);
        for (WebElement element : elements) {
            if (!element.isDisplayed()) {
                continue;
            }
            try {
                DynamicElementDetectionResult elementDetectionResult = dynamicElementDetector.
                        checkIfActionCausesPageChanges(driver, element, actionFactory, ignoreAreas);
                boolean actionOccurred = elementDetectionResult.getDifference() > DIFFERENCE_THRESHOLD;
                if (actionOccurred) {
                    String xPath = webElementXPathRetriever.retrieveElementXPath(driver, element);
                    seleniumTestGenerator.generateTest(getHoverAction(xPath), elementDetectionResult, counter);
                    counter++;
                }
            } catch (Exception e) {
                log.warn("Exception during test generation for webElement. It will be omitted.");
            }
        }
        log.info("Number of generated tests: {}", counter - 1);
    }
}
