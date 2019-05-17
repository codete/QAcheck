package com.codete.regression.screenshot;

import com.codete.regression.testengine.teststep.TestStep;
import org.springframework.stereotype.Service;

@Service
public class ScreenshotsDtoFactory {

    private final ScreenshotStorage screenshotStorage;
    private final ImageIOWrapper imageIOWrapper;

    public ScreenshotsDtoFactory(ScreenshotStorage screenshotStorage, ImageIOWrapper imageIOWrapper) {
        this.screenshotStorage = screenshotStorage;
        this.imageIOWrapper = imageIOWrapper;
    }

    public ScreenshotsDto createScreenshotsDto(TestStep testStep) {
        String baselineScreenshotPath = testStep.getComparisonSettings().getBaselineScreenshotPath();
        String diffScreenshotPath = testStep.getComparisonResult().getDiffScreenshotPath();
        String currentScreenshotPath = testStep.getComparisonResult().getCurrentScreenshotPath();
        ScreenshotsDto screenshotsDto = new ScreenshotsDto();
        screenshotsDto.setBaselineScreenshot(convertImageToBase64(baselineScreenshotPath));
        screenshotsDto.setCurrentScreenshot(convertImageToBase64(currentScreenshotPath));
        screenshotsDto.setDiffScreenshot(convertImageToBase64(diffScreenshotPath));
        return screenshotsDto;
    }

    private String convertImageToBase64(String screenshotPath) {
        if (screenshotPath != null) {
            ScreenshotBufferedImage screenshotBufferedImage = screenshotStorage.readScreenshot(screenshotPath);
            return imageIOWrapper.convertScreenshotToBase64(screenshotBufferedImage.getScreenshot());
        } else {
            return null;
        }
    }

}
