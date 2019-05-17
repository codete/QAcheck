package com.codete.regression.screenshot;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.comparator.ImageComparator;
import com.codete.regression.screenshot.comparator.ImageComparisonResult;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.teststep.ComparisonResult;
import com.codete.regression.testengine.teststep.ComparisonResult.ComparisonResultBuilder;
import com.codete.regression.testengine.teststep.TestStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
@Slf4j
public class ScreenshotComparator {

    private static final String DIFF_IMAGE_POSTFIX = "_diff";
    private final ImageIOWrapper imageIOWrapper;
    private final ScreenshotStorage screenshotStorage;
    private final ImageComparator imageComparator;

    public ScreenshotComparator(ImageIOWrapper imageIOWrapper, ScreenshotStorage screenshotStorage,
                                ImageComparator imageComparator) {
        this.imageIOWrapper = imageIOWrapper;
        this.screenshotStorage = screenshotStorage;
        this.imageComparator = imageComparator;
    }

    public ComparisonResult saveAndCompareScreenshotWithTheBaseline(ComparisonSettings comparisonSettings,
                                                                    String screenshotStorageLocation,
                                                                    Screenshot screenshot) {
        BufferedImage currentImage = imageIOWrapper.convertToBufferedImage(screenshot.getBytes());
        ScreenshotBufferedImage currentScreenshot = screenshotStorage.saveScreenshot(currentImage,
                screenshotStorageLocation, screenshot.getStepName());
        ComparisonResultBuilder comparisonResultBuilder = ComparisonResult.builder()
                .currentScreenshotPath(currentScreenshot.getRelativePath());
        if (comparisonSettings.getBaselineScreenshotPath() != null) {
            ImageComparisonResult imageComparisonResult = compareScreenshot(comparisonSettings, currentScreenshot);
            return handleImageComparisonResult(imageComparisonResult, comparisonResultBuilder, screenshot.getStepName(),
                    screenshotStorageLocation);
        } else {
            log.info("This is first run of this test step. " +
                    "Baseline was saved, comparison will not be executed.");
            return comparisonResultBuilder
                    .passed(true)
                    .build();
        }
    }

    public ComparisonResult compareTestStepScreenshotWithTheBaseline(TestStep testStep) {
        ComparisonResult previousComparisonResult = testStep.getComparisonResult();
        ComparisonSettings comparisonSettings = testStep.getComparisonSettings();
        ComparisonResultBuilder comparisonResultBuilder = ComparisonResult.builder()
                .currentScreenshotPath(previousComparisonResult.getCurrentScreenshotPath());

        if (comparisonSettings.getBaselineScreenshotPath() == null) {
            log.info("This is first run of this test step. Comparison will not be executed.");
            return comparisonResultBuilder
                    .passed(true)
                    .build();
        }

        ScreenshotBufferedImage currentScreenshot = screenshotStorage.readScreenshot(previousComparisonResult.getCurrentScreenshotPath());
        ImageComparisonResult imageComparisonResult = compareScreenshot(comparisonSettings, currentScreenshot);
        return handleImageComparisonResult(
                imageComparisonResult,
                comparisonResultBuilder,
                testStep.getStepName(),
                screenshotStorage.getScreenshotStorageLocation(currentScreenshot)
        );
    }

    private ImageComparisonResult compareScreenshot(ComparisonSettings comparisonSettings,
                                                    ScreenshotBufferedImage currentScreenshot) {
        ScreenshotBufferedImage baselineScreenshot = screenshotStorage.readScreenshot(comparisonSettings
                .getBaselineScreenshotPath());
        ImageComparisonResult comparisonResult = imageComparator.compare(baselineScreenshot.getScreenshot(),
                currentScreenshot.getScreenshot(), comparisonSettings);
        log.info(String.format("Compared images: %s with %s, difference: %3.2f percent",
                baselineScreenshot.getRelativePath(), currentScreenshot.getRelativePath(),
                comparisonResult.getDifference()));
        return comparisonResult;
    }

    private ComparisonResult handleImageComparisonResult(ImageComparisonResult comparisonResult,
                                                         ComparisonResultBuilder comparisonResultBuilder,
                                                         String stepName, String screenshotStorageLocation) {
        if (!comparisonResult.isPassed()) {
            ScreenshotBufferedImage diffFile = screenshotStorage.saveScreenshot(comparisonResult.getDiffImage(),
                    screenshotStorageLocation, stepName + DIFF_IMAGE_POSTFIX);
            comparisonResultBuilder.diffScreenshotPath(diffFile.getRelativePath());
        }
        return comparisonResultBuilder.passed(comparisonResult.isPassed())
                .diffPercentage(comparisonResult.getDifference())
                .build();
    }
}
