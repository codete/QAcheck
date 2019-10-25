package com.codete.regression.screenshot;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.api.testengine.TestRunRequest;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.testrun.TestRun;
import com.codete.regression.testengine.userapp.UserApp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ScreenshotStorage {

    ScreenshotBufferedImage readScreenshot(String screenshotPath);

    ScreenshotBufferedImage saveScreenshot(BufferedImage image, String screenshotStorageLocation, String fileName);

    void deleteScreenshots(String screenshotsPath);

    void deleteByFileName(String screenshotStorageLocation, String fileName);

    default String getScreenshotStorageLocation(TestCase testCase, TestRun testRun, TestRunRequest request) {
        return testCase.getUsername() + File.separator + request.getAppName()
                + File.separator + testCase.getUuid() + File.separator + testRun.getUuid();
    }

    default String getScreenshotStorageLocation(ScreenshotBufferedImage screenshotBufferedImage) {
        String relativePath = screenshotBufferedImage.getRelativePath();
        return relativePath.substring(0, relativePath.lastIndexOf(File.separator));
    }

    default String getUploadedScreenshotStorageLocation(TestCase testCase) {
        return testCase.getUsername() + File.separator + testCase.getUserApp().getAppName()
                + File.separator + testCase.getUuid() + File.separator + "uploaded";
    }

    default String getTestCaseStorageLocation(TestCase testCase) {
        return testCase.getUsername() + File.separator + testCase.getUserApp().getAppName() + File.separator + testCase.getUuid();
    }

    default String getUserAppStorageLocation(UserApp userApp) {
        return userApp.getUsername() + File.separator + userApp.getAppName();
    }

}
