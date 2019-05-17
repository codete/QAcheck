package com.codete.regression.api.testgenerator.engine.mouseclick;

import com.codete.regression.api.testgenerator.engine.ActionFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ClickActionFactory implements ActionFactory {

    @Override
    public void performInitAction(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).moveByOffset(Integer.MAX_VALUE, 0).build().perform();
    }

    @Override
    public void performAction(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).click().perform();
    }
}
