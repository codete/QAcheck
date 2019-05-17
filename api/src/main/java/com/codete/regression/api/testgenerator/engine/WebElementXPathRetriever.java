package com.codete.regression.api.testgenerator.engine;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Slf4j
public class WebElementXPathRetriever {

    private static final String CREATE_GET_XPATH_JS_FUNCTION = "getXpath=function(c){if(c.id!=='')" +
            "{return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;" +
            "for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return getXpath(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}" +
            "if(d.nodeType===1&&d.tagName===c.tagName){a++}}};";
    private static final String GET_XPATH_JS_COMMAND = "return getXpath(arguments[0]);";

    public String retrieveElementXPath(WebDriver driver, WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript(CREATE_GET_XPATH_JS_FUNCTION);
        String xPath = (String) ((JavascriptExecutor) driver).executeScript(GET_XPATH_JS_COMMAND, webElement);
        xPath = xPath.replaceAll("\"", "'");
        if (!xPath.startsWith("id")) {
            xPath = "//" + xPath;
        }
        return xPath;
    }
}
