package com.codete.regression.examples;

import com.codete.regression.api.testgenerator.TestCaseGenerator;
import com.codete.regression.api.testgenerator.TestCaseGeneratorRequest;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

public class SeleniumTestGenerator {

    private static TestCaseGenerator testCaseGenerator = new TestCaseGenerator(AccountConfig.SERVER_URL,
            AccountConfig.API_KEY);

    public static void main(String[] args) {
        runTestCaseGeneratorForOnePage();
//        runTestCaseGeneratorWithCrawler();
    }

    private static void runTestCaseGeneratorForOnePage() {
        TestCaseGeneratorRequest testCaseGeneratorRequest = createGeneratorRequest();
        RemoteWebDriver driver = createWebDriver();
        testCaseGenerator.generateTestCases(testCaseGeneratorRequest, driver);
        driver.quit();
    }

    private static void runTestCaseGeneratorWithCrawler() {
        TestCaseGeneratorRequest testCaseGeneratorRequest = createGeneratorRequest();
        testCaseGeneratorRequest.setRunWithCrawler(true);
        testCaseGeneratorRequest.setCrawlerMaxPagesToVisit(2);
        RemoteWebDriver driver = createWebDriver();
        testCaseGenerator.generateTestCases(testCaseGeneratorRequest, driver);
        driver.quit();
    }

    private static TestCaseGeneratorRequest createGeneratorRequest() {
        TestCaseGeneratorRequest testCaseGeneratorRequest = new TestCaseGeneratorRequest();
        testCaseGeneratorRequest.setAppName("generatedTests");
        testCaseGeneratorRequest.setGeneratedTestDirectoryPath("C:\\repository\\regression-testing-platform\\examples\\src\\test\\java");
        testCaseGeneratorRequest.setScreenshotsDirectoryPath("C:\\repository\\regression-testing-platform\\examples\\testsGenerator");
        testCaseGeneratorRequest.setPageUrl("https://codete.com/services");
        testCaseGeneratorRequest.setTestPackage("com.codete.regression.generated");
        testCaseGeneratorRequest.setClassNamePrefix("Codete");
        return testCaseGeneratorRequest;
    }

    private static RemoteWebDriver createWebDriver() {
        RemoteWebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return driver;
    }
}
