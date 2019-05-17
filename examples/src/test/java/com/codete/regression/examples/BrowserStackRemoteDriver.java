package com.codete.regression.examples;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserStackRemoteDriver {

    private static final String USERNAME = "";
    private static final String AUTOMATE_KEY = "";
    private static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static WebDriver crateBrowserStackWebDriver() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browser", "Chrome");
        caps.setCapability("browser_version", "62.0");
        caps.setCapability("os", "Windows");
        caps.setCapability("os_version", "10");
        caps.setCapability("resolution", "2048x1536");
        Dimension dimension = new Dimension(2048, 1536);
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        driver.manage().window().setSize(dimension);
        return driver;
    }
}
