package com.codete.regression.api.testgenerator.engine.singlepagepath;

import com.codete.regression.api.testgenerator.engine.WebElementXPathRetriever;
import com.codete.regression.api.testgenerator.engine.mouseclick.ClickElementDetector;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
class SinglePagePathAnalyzer {

    private static final String[] TAGS_TO_ANALYZE = {"div", "a", "button"};
    private final WebElementXPathRetriever webElementXPathRetriever = new WebElementXPathRetriever();
    private final ClickElementDetector clickElementDetector = new ClickElementDetector();

    List<SinglePagePath> searchPathsForClickElements(RemoteWebDriver driver, List<String> clickableElements) {
        List<String> notDisplayedWebElements = getNotDisplayedElements(driver);
        List<SinglePagePath> singlePagePaths = new ArrayList<>();
        String baseUrl = driver.getCurrentUrl();
        for (String elementXPath : clickableElements) {
            driver.get(baseUrl);
            Collection<String> occurredElements = findElementsWhichOccurredAfterClick(driver,
                    Collections.singletonList(elementXPath), notDisplayedWebElements);
            if (occurredElements.size() > 0) {
                singlePagePaths.add(new SinglePagePath(elementXPath, occurredElements));
            }
        }
        driver.get(baseUrl);
        return singlePagePaths;
    }

    List<SinglePagePath> searchForNewPaths(RemoteWebDriver driver, SinglePagePath singlePagePath,
                                           Set<String> reachedElements) {
        List<String> notDisplayedWebElements = getNotDisplayedElements(driver);
        String baseUrl = driver.getCurrentUrl();
        List<SinglePagePath> singlePagePaths = new ArrayList<>();
        for (String elementXPath : singlePagePath.getOccurredElements()) {
            driver.get(baseUrl);
            LinkedList<String> path = new LinkedList<>(singlePagePath.getClickElementsPath());
            path.add(elementXPath);
            Collection<String> occurredElements = findElementsWhichOccurredAfterClick(driver, path,
                    notDisplayedWebElements);
            if (occurredElements.size() > 0 && !reachedElements.containsAll(occurredElements)) {
                singlePagePaths.add(new SinglePagePath(path, occurredElements));
            }
        }
        driver.get(baseUrl);
        return singlePagePaths;
    }

    private List<String> getNotDisplayedElements(RemoteWebDriver driver) {
        List<WebElement> elements = new ArrayList<>();
        Stream.of(TAGS_TO_ANALYZE).forEach(tag -> elements.addAll(driver.findElementsByTagName(tag)));
        return elements.stream()
                .filter(webElement -> !webElement.isDisplayed())
                .map(element -> webElementXPathRetriever.retrieveElementXPath(driver, element))
                .collect(Collectors.toList());
    }

    private Collection<String> findElementsWhichOccurredAfterClick(RemoteWebDriver driver,
                                                                   Collection<String> path,
                                                                   Collection<String> notDisplayedWebElements) {
        List<String> occurredElements = new ArrayList<>();
        clickThroughPath(driver, path);
        for (String notDisplayedWebElement : notDisplayedWebElements) {
            try {
                WebElement element = driver.findElementByXPath(notDisplayedWebElement);
                if (element.isDisplayed()) {
                    occurredElements.add(notDisplayedWebElement);
                }
            } catch (WebDriverException e) {
                //Ignore, element will be omitted
            }
        }
        return filterElementsThatRedirectToAnotherPage(driver, path, occurredElements);
    }

    private Collection<String> filterElementsThatRedirectToAnotherPage(RemoteWebDriver driver, Collection<String> path,
                                                                       Collection<String> occurredElements) {
        List<String> elementsThatDoNotRedirectAfterClick = new ArrayList<>();
        String baseUrl = driver.getCurrentUrl();
        String originalHandle = driver.getWindowHandle();
        Set<String> elementsToAnalyze = new HashSet<>(occurredElements);
        elementsToAnalyze.addAll(path);
        for (String occurredElementXPath : elementsToAnalyze) {
            driver.get(baseUrl);
            clickThroughPath(driver, path);
            clickElementDetector.checkIfClickDoNotRedirectToAnotherPage(driver, baseUrl, originalHandle,
                    occurredElementXPath)
                    .ifPresent(elementsThatDoNotRedirectAfterClick::add);
        }
        return elementsThatDoNotRedirectAfterClick;
    }

    private void clickThroughPath(RemoteWebDriver driver, Collection<String> path) {
        for (String pathElement : path) {
            try {
                WebElement element = driver.findElementByXPath(pathElement);
                if (element.isDisplayed() && element.isEnabled()) {
                    Actions action = new Actions(driver);
                    action.moveToElement(element).click().build().perform();
                } else {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                log.warn("Unable to click on the element");
            } catch (NoSuchElementException | ElementNotVisibleException e) {
                log.warn("Element not visible. Won't be analyzed.");
            }
        }
    }
}
