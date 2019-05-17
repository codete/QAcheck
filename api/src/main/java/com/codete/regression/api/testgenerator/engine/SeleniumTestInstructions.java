package com.codete.regression.api.testgenerator.engine;

import java.util.List;

public class SeleniumTestInstructions {

    private static final String WEB_ELEMENT_KEY = "{webElementXPath}";
    private static final String WEB_ELEMENT_ACTION = "WebElement webElement = driver.findElement(By.xpath(\"" + WEB_ELEMENT_KEY + "\"));\n" +
            "        ((JavascriptExecutor) driver).executeScript(SCROLL_ELEMENT_INTO_MIDDLE, webElement);\n" +
            "        Actions action = new Actions(driver);\n";

    private static final String HOVER_ACTION = WEB_ELEMENT_ACTION +
            "        action.moveToElement(webElement).perform();";

    private static final String CLICK_ACTION = WEB_ELEMENT_ACTION +
            "        action.moveToElement(webElement).click().perform();";

    private static final String GO_THROUGH_PATH = "String[] path = new String[]{" + WEB_ELEMENT_KEY + "};\n" +
            "            for(String pathElement: path) {\n" +
            "                WebElement webElement = driver.findElement(By.xpath(pathElement));\n" +
            "                ((JavascriptExecutor) driver).executeScript(SCROLL_ELEMENT_INTO_MIDDLE, webElement);\n" +
            "                Actions action = new Actions(driver);\n" +
            "                action.moveToElement(webElement).click().perform();\n" +
            "            }";

    public static String getClickAction(String xPath) {
        return CLICK_ACTION.replace(WEB_ELEMENT_KEY, xPath);
    }

    public static String getHoverAction(String xPath) {
        return HOVER_ACTION.replace(WEB_ELEMENT_KEY, xPath);
    }

    public static String getGoThroughPathAction(List<String> path) {
        StringBuilder pathBuilder = new StringBuilder();
        path.forEach(element -> pathBuilder.append("\"").append(element).append("\"").append(","));
        pathBuilder.deleteCharAt(pathBuilder.length() - 1);
        return GO_THROUGH_PATH.replace(WEB_ELEMENT_KEY, pathBuilder.toString());
    }

}
