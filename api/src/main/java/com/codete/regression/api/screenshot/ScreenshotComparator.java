package com.codete.regression.api.screenshot;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.screenshot.drivers.ScreenshotTaker;
import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import com.codete.regression.api.testengine.TestRunConfig;
import com.codete.regression.api.testengine.TestRunRequest;
import com.codete.regression.api.testengine.TestRunService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.codete.regression.api.testengine.TestRunPropertyValidator.APP_NAME_MAX_LENGTH;
import static com.codete.regression.api.testengine.TestRunPropertyValidator.APP_NAME_MIN_LENGTH;
import static com.codete.regression.api.testengine.TestRunPropertyValidator.TEST_NAME_MAX_LENGTH;
import static com.codete.regression.api.testengine.TestRunPropertyValidator.TEST_NAME_MIN_LENGTH;
import static com.codete.regression.api.testengine.TestRunPropertyValidator.validate;

@Slf4j
public class ScreenshotComparator {

    private final ScreenshotTaker screenshotTaker;
    private final ScreenshotTaker multipleScreenshotsTaker;
    private final String appName;
    private final TestRunService testRunService;
    private final EnvironmentSettingsRetriever environmentSettingsRetriever;
    private final TestRunConfig testRunConfig = new TestRunConfig();
    private final ScreenshotTakerConfig screenshotTakerConfig = new ScreenshotTakerConfig();

    public ScreenshotComparator(String serverUrl, String apiKey, String appName, ScreenshotTakerType screenshotTakerType) {
        validate(appName, "appName", APP_NAME_MIN_LENGTH, APP_NAME_MAX_LENGTH);
        this.testRunService = new TestRunService(new ServerConnection(serverUrl, apiKey));
        this.environmentSettingsRetriever = new EnvironmentSettingsRetriever(screenshotTakerType);
        this.appName = appName;
        this.screenshotTaker = screenshotTakerType.getScreenshotTaker();
        this.multipleScreenshotsTaker = screenshotTakerType.getMultipleScreenshotsTaker();
    }

    public boolean compareScreenshotWithBaseline(WebDriver driver, String testName) {
        validate(testName, "testName", TEST_NAME_MIN_LENGTH, TEST_NAME_MAX_LENGTH);
        List<Screenshot> currentScreenshots = screenshotTaker.takeScreenshots(driver, screenshotTakerConfig);
        return sendScreenshotsToTheServerAndCompare(driver, testName, currentScreenshots, false);
    }

    public boolean ignoreDynamicElementsAndCompareWithBaseline(WebDriver driver, String testName) {
        validate(testName, "testName", TEST_NAME_MIN_LENGTH, TEST_NAME_MAX_LENGTH);
        List<Screenshot> currentScreenshots = multipleScreenshotsTaker.takeScreenshots(driver, screenshotTakerConfig);
        return sendScreenshotsToTheServerAndCompare(driver, testName, currentScreenshots, true);
    }

    public void setAllowedDifferencePercentage(double percentage) {
        this.testRunConfig.setAllowedDifferencePercentage(percentage);
    }

    public void setAllowedDelta(double allowedDelta) {
        this.testRunConfig.setAllowedDelta(allowedDelta);
    }

    public void setHorizontalShift(int horizontalShift) {
        this.testRunConfig.setHorizontalShift(horizontalShift);
    }

    public void setVerticalShift(int verticalShift) {
        this.testRunConfig.setVerticalShift(verticalShift);
    }

    public void setShowDetectedShift(boolean showDetectedShift) {
        this.testRunConfig.setShowDetectedShift(showDetectedShift);
    }

    public void setPerceptualMode(boolean perceptualMode) {
        this.testRunConfig.setPerceptualMode(perceptualMode);
    }

    public void setFullPageScreenshot(boolean fullPageScreenshot) {
        this.screenshotTakerConfig.setFullPageScreenshot(fullPageScreenshot);
    }

    private boolean sendScreenshotsToTheServerAndCompare(WebDriver driver, String testName,
                                                         List<Screenshot> currentScreenshots,
                                                         boolean detectDynamicElements) {
        EnvironmentSettings environmentSettings = environmentSettingsRetriever.retrieveEnvironmentSettings(driver);
        TestRunRequest request = new TestRunRequest(appName, testName, environmentSettings,
                currentScreenshots, detectDynamicElements, testRunConfig);
        log.info("Sending screenshot to comparison.");
        return testRunService.compareImagesWithTheBaseline(request);
    }

}
