package com.codete.regression.api.testgenerator.engine;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Slf4j
class ScreenshotSaver {

    private static final String IMAGE_EXTENSION = "png";
    private static final String SCREENSHOT_BEFORE_ACTION_POSTFIX = "ABefore";
    private static final String SCREENSHOT_AFTER_ACTION_POSTFIX = "BAfter";
    private static final String SCREENSHOT_DIFF_POSTFIX = "CDiff";

    void saveScreenshots(DynamicElementDetectionResult imageComparatorResponse,
                         String directoryPath, int elementCounter) {
        (new File(directoryPath)).mkdirs();
        saveScreenshot(imageComparatorResponse.getDiffImage(), directoryPath,
                elementCounter + SCREENSHOT_DIFF_POSTFIX);
        saveScreenshot(imageComparatorResponse.getScreenshotBefore(), directoryPath,
                elementCounter + SCREENSHOT_BEFORE_ACTION_POSTFIX);
        saveScreenshot(imageComparatorResponse.getScreenshotAfter(), directoryPath,
                elementCounter + SCREENSHOT_AFTER_ACTION_POSTFIX);
    }

    private void saveScreenshot(byte[] screenshot, String directoryPath, String fileName) {
        String path = directoryPath + File.separator + fileName + "." + IMAGE_EXTENSION;
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(path));) {
            out.write(screenshot);
        } catch (Exception e) {
            log.error("Couldn't save image.", e);
        }
    }
}
