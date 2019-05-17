package com.codete.regression.api.testgenerator.engine.mouseclick;

import com.codete.regression.api.testgenerator.engine.WebElementXPathRetriever;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ClickElementDetector {

    private static final String[] TAGS_TO_ANALYZE = {"button"};
    private final WebElementXPathRetriever webElementXPathRetriever = new WebElementXPathRetriever();

    public List<String> searchElements(RemoteWebDriver driver) {
        List<WebElement> elements = new ArrayList<>();
        Stream.of(TAGS_TO_ANALYZE).forEach(tag -> elements.addAll(driver.findElementsByTagName(tag)));
        // We have to retrieve xPaths, because after reloading page we won't be able to use WebElements references
        List<String> elementXPaths = elements.stream()
                .filter(WebElement::isDisplayed)
                .map(element -> webElementXPathRetriever.retrieveElementXPath(driver, element))
                .collect(Collectors.toList());
        List<String> elementsThatDoNotRedirectAfterClick = new ArrayList<>();
        String baseUrl = driver.getCurrentUrl();
        String originalHandle = driver.getWindowHandle();
        for (String elementXPath : elementXPaths) {
            driver.get(baseUrl);
            checkIfClickDoNotRedirectToAnotherPage(driver, baseUrl, originalHandle, elementXPath)
                    .ifPresent(elementsThatDoNotRedirectAfterClick::add);
        }
        driver.get(baseUrl);
        return elementsThatDoNotRedirectAfterClick;
    }

    public Optional<String> checkIfClickDoNotRedirectToAnotherPage(RemoteWebDriver driver, String baseUrl,
                                                                   String windowOriginalHandle, String elementXPath) {
        Optional<String> result = Optional.empty();
        try {
            WebElement element = driver.findElementByXPath(elementXPath);
            if (element.isDisplayed() && element.isEnabled()) {
                Actions action = new Actions(driver);
                action.moveToElement(element).click().build().perform();
                if (driver.getWindowHandles().size() > 1) {
                    closeAllNewlyOpenedTabs(windowOriginalHandle, driver);
                } else if (driver.getCurrentUrl().equals(baseUrl)) {
                    result = Optional.of(elementXPath);
                }
            }
        } catch (StaleElementReferenceException e) {
            log.warn("Unable to click on the element");
        } catch (NoSuchElementException | ElementNotVisibleException e) {
            log.warn("Element not visible. Won't be analyzed.");
        }
        return result;
    }

    private void closeAllNewlyOpenedTabs(String originalHandle, RemoteWebDriver driver) {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }
        driver.switchTo().window(originalHandle);
    }

}
