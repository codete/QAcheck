package com.codete.regression.examples.browser;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

// Example filter which is used to delete all dynamic elements on page before taking screenshots
// This filter is written for Codete page
@Slf4j
class CodeteDynamicElementsFilter {

    private static final String GET_ALL_ELEMENTS_BY_CLASS_NAME = "var elements = document.getElementsByClassName('%s');";
    private static final String GET_ALL_ELEMENTS_BY_TAG_NAME = "var elements = document.getElementsByTagName('%s');";
    private static final String ELEMENTS_FOR_LOOP = "for (var i = 0; i < elements.length; i++) {%s};";

    void deleteAllDynamicElementsOnPage(WebDriver driver) {
        JavascriptExecutor jsx = (JavascriptExecutor) driver;
        sleep(1500);
        removeAllElementByTagName(jsx, "video");
        removeAllElementsByClassName(jsx, "header-clients");
        removeAllElementsByClassName(jsx, "slider-about-elements");
        removeAllElementsByClassName(jsx, "navbar");
        sleep(1000);
    }

    private void removeAllElementByTagName(JavascriptExecutor driver, String tagName) {
        driver.executeScript(String.format(GET_ALL_ELEMENTS_BY_TAG_NAME, tagName) +
                String.format(ELEMENTS_FOR_LOOP, "elements[i].load();"));
    }

    private void removeAllElementsByClassName(JavascriptExecutor driver, String className) {
        driver.executeScript(String.format(GET_ALL_ELEMENTS_BY_CLASS_NAME, className) +
                String.format(ELEMENTS_FOR_LOOP, "elements[i].remove();"));
    }

    private void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

}
