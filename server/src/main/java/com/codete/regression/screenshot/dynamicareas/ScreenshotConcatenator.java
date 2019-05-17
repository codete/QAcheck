package com.codete.regression.screenshot.dynamicareas;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.ImageIOWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
class ScreenshotConcatenator {

    private final ImageIOWrapper imageIOWrapper;

    public ScreenshotConcatenator(ImageIOWrapper imageIOWrapper) {
        this.imageIOWrapper = imageIOWrapper;
    }

    List<BufferedImage> concatenateScreenshotParts(List<Screenshot> screenshots) {
        log.info("Concatenating screenshot parts.");
        Map<Integer, List<BufferedImage>> screenshotsGroupByScrollIterationNumber =
                groupScreenshotsByScrollIterationNumber(screenshots);
        List<BufferedImage> concatenatedScreenshots = new ArrayList<>();
        for (Map.Entry<Integer, List<BufferedImage>> entry : screenshotsGroupByScrollIterationNumber.entrySet()) {
            concatenatedScreenshots.add(concatenateScreenshots(entry.getValue()));
        }
        return concatenatedScreenshots;
    }

    private BufferedImage concatenateScreenshots(List<BufferedImage> screenshots) {
        int concatenatedImageHeight = 0;
        int concatenatedImageWidth = screenshots.get(0).getWidth();
        for (BufferedImage bufferedImage : screenshots) {
            concatenatedImageHeight += bufferedImage.getHeight();
        }
        BufferedImage concatImage = new BufferedImage(concatenatedImageWidth, concatenatedImageHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = concatImage.createGraphics();
        int currentHeight = 0;
        for (BufferedImage bufferedImage : screenshots) {
            graphics2D.drawImage(bufferedImage, 0, currentHeight, null);
            currentHeight += bufferedImage.getHeight();
        }
        graphics2D.dispose();
        return concatImage;
    }

    private Map<Integer, List<BufferedImage>> groupScreenshotsByScrollIterationNumber(List<Screenshot> screenshots) {
        Map<Integer, List<BufferedImage>> groupScreenshotsByIterationsNumber = new HashMap<>();
        Screenshot previousScreenshot = null;
        int iterationCounter = 0;
        List<BufferedImage> screenshotsForOneIteration = new ArrayList<>();
        for (Screenshot screenshot : screenshots) {
            if (previousScreenshot != null && previousScreenshot.getStepName().compareTo(screenshot.getStepName()) > 0) {
                screenshotsForOneIteration = new ArrayList<>();
                iterationCounter++;
                groupScreenshotsByIterationsNumber.put(iterationCounter, screenshotsForOneIteration);
            }
            if (previousScreenshot != null) {
                screenshotsForOneIteration.add(imageIOWrapper.convertToBufferedImage(screenshot.getBytes()));
            }
            previousScreenshot = screenshot;
        }
        iterationCounter++;
        groupScreenshotsByIterationsNumber.put(iterationCounter, screenshotsForOneIteration);
        return groupScreenshotsByIterationsNumber;
    }

}
