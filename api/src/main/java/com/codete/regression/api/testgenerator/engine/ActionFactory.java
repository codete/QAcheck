package com.codete.regression.api.testgenerator.engine;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface ActionFactory {

    void performInitAction(WebDriver driver, WebElement webElement);

    void performAction(WebDriver driver, WebElement webElement);
}
