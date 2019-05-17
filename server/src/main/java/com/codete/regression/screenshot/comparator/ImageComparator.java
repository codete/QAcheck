package com.codete.regression.screenshot.comparator;

import com.codete.regression.screenshot.comparator.color.ColorBitmap;
import com.codete.regression.screenshot.comparator.color.ColorVector;
import com.codete.regression.screenshot.comparator.color.delta.ColorDeltaCalculator;
import com.codete.regression.screenshot.comparator.color.delta.ColorDeltaCalculatorFactory;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageComparator {
    private final ColorDeltaCalculatorFactory deltaCalculatorFactory;
    private final BufferedImageToColorBitmapConverter bitmapConverter;
    private final AntiAliasingDetector antiAliasingDetector;
    private final int shiftHighlightColor = new RgbPixel(255, 255, 140, 0).toRgbInt();
    private final int diffHighlightColor = new RgbPixel(255, 230, 0, 0).toRgbInt();

    public ImageComparisonResult compare(BufferedImage baselinePicture, BufferedImage currentPicture,
                                         ComparisonSettings comparisonSettings) {
        IgnoreAreasProcessor ignoreAreasProcessor = new IgnoreAreasProcessor(comparisonSettings.getIgnoreAreas());
        return compare(baselinePicture, currentPicture, comparisonSettings, ignoreAreasProcessor);
    }

    ImageComparisonResult compare(BufferedImage baselinePicture, BufferedImage currentPicture,
                                  ComparisonSettings comparisonSettings, IgnoreAreasProcessor ignoreAreasProcessor) {
        checkImagesSize(baselinePicture, currentPicture);
        return calculateDifference(baselinePicture, currentPicture, ignoreAreasProcessor, comparisonSettings);
    }

    private void checkImagesSize(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();
        int width2 = image2.getWidth();
        int height2 = image2.getHeight();
        if (width != width2 || height != height2) {
            throw new IllegalArgumentException(String.format("Images must have the same dimensions: (%d,%d) vs. (%d,%d)",
                    width, height, width2, height2));
        }
    }

    private BufferedImage createDarkenedImage(BufferedImage image) {
        BufferedImage diffImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        boolean hasAlphaChannel = image.getAlphaRaster() != null;
        float[] scaleFactors = hasAlphaChannel ? new float[]{0.3f, 0.3f, 0.3f, 1f} : new float[]{0.3f, 0.3f, 0.3f};
        float[] offsets = hasAlphaChannel ? new float[]{0, 0, 0, 0} : new float[]{0, 0, 0};
        RescaleOp darkenFilterOp = new RescaleOp(scaleFactors, offsets, null);
        darkenFilterOp.filter(image, diffImage);
        return diffImage;
    }

    private ImageComparisonResult calculateDifference(BufferedImage image1,
                                                      BufferedImage image2,
                                                      IgnoreAreasProcessor ignoreAreasProcessor,
                                                      ComparisonSettings comparisonSettings) {

        int width = image1.getWidth();
        int height = image1.getHeight();
        double allowedDelta = comparisonSettings.getAllowedDelta();
        boolean showDetectedShift = comparisonSettings.isShowDetectedShift();

        BufferedImage diffImage = createDarkenedImage(image2);
        List<Point> diffPoints = new ArrayList<>();

        ColorDeltaCalculator deltaCalculator = deltaCalculatorFactory.create(comparisonSettings);

        ColorBitmap colorBitmap1 = bitmapConverter.convert(image1, deltaCalculator);
        ColorBitmap colorBitmap2 = bitmapConverter.convert(image2, deltaCalculator);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point point = new Point(x, y);

                ColorVector color1 = colorBitmap1.getColor(x, y);
                ColorVector color2 = colorBitmap2.getColor(x, y);

                if (!ignoreAreasProcessor.isPixelIgnored(point)) {
                    double delta = deltaCalculator.calculate(color1, color2);
                    if (delta > allowedDelta) {
                        if (antiAliasingDetector.isAntiAliasing(colorBitmap1, colorBitmap2, point, comparisonSettings, deltaCalculator)) {
                            if (showDetectedShift) {
                                diffImage.setRGB(x, y, shiftHighlightColor);
                            }
                        } else {
                            diffPoints.add(point);
                            diffImage.setRGB(x, y, diffHighlightColor);
                        }
                    }
                }
            }
        }

        long numberOfPixels = width * height;
        double diff = (100.0 * diffPoints.size() / numberOfPixels);
        boolean passed = diff <= comparisonSettings.getAllowedDifferencePercentage();
        return new ImageComparisonResult(diffImage, diffPoints, diff, passed);
    }

}
