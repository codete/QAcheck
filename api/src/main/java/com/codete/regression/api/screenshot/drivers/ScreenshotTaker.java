package com.codete.regression.api.screenshot.drivers;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.screenshot.ScreenshotTakerConfig;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface ScreenshotTaker {

    String SCREENSHOT_PART_PREFIX = "part_";

    List<Screenshot> takeScreenshots(WebDriver driver, ScreenshotTakerConfig screenshotTakerConfig);
}
