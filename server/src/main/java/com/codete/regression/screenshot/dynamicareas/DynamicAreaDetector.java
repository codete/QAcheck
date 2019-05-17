package com.codete.regression.screenshot.dynamicareas;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.ImageIOWrapper;
import com.codete.regression.screenshot.comparator.ImageComparator;
import com.codete.regression.screenshot.comparator.ImageComparisonResult;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DynamicAreaDetector {

    private final ImageComparator imageComparator;
    private final ImageIOWrapper imageIOWrapper;
    private final BoundingBoxCalculator boundingBoxCalculator;

    public DynamicAreaDetector(ImageComparator imageComparator, ImageIOWrapper imageIOWrapper,
                               BoundingBoxCalculator boundingBoxCalculator) {
        this.imageComparator = imageComparator;
        this.imageIOWrapper = imageIOWrapper;
        this.boundingBoxCalculator = boundingBoxCalculator;
    }

    public List<IgnoreArea> createIgnoreAreasForDynamicElements(ComparisonSettings comparisonSettings,
                                                                List<Screenshot> screenshots) {
        List<BufferedImage> bufferedImages = screenshots.stream()
                .map(Screenshot::getBytes)
                .map(imageIOWrapper::convertToBufferedImage)
                .collect(Collectors.toList());
        return createIgnoreAreasForDynamicElementsBufferedImages(comparisonSettings, bufferedImages);
    }

    List<IgnoreArea> createIgnoreAreasForDynamicElementsBufferedImages(ComparisonSettings comparisonSettings,
                                                                       List<BufferedImage> screenshots) {
        log.info("Generating ignore areas.");
        List<ImageComparisonResult> imageComparisonResults = runScreenshotComparisons(comparisonSettings, screenshots);
        Set<Point> diffPixels = mergeDiffPointsAndSort(imageComparisonResults);
        return boundingBoxCalculator.createBoundingBoxes(diffPixels);
    }

    private List<ImageComparisonResult> runScreenshotComparisons(ComparisonSettings comparisonSettings,
                                                                 List<BufferedImage> screenshots) {
        BufferedImage firstScreenshot = screenshots.get(0);
        List<ImageComparisonResult> imageComparisonResults = new ArrayList<>();
        for (int i = 1; i < screenshots.size(); i++) {
            BufferedImage currentImage = screenshots.get(i);
            ImageComparisonResult imageComparisonResult = imageComparator.compare(firstScreenshot, currentImage,
                    comparisonSettings);
            imageComparisonResults.add(imageComparisonResult);
        }
        return imageComparisonResults;
    }

    /**
     * Sorting points before calculating ignored areas helps to create fewer but more accurate areas
     */
    private Set<Point> mergeDiffPointsAndSort(List<ImageComparisonResult> imageComparisonResults) {
        return imageComparisonResults.stream()
                .map(ImageComparisonResult::getDiffPixels)
                .flatMap(Collection::stream)
                .sorted((point1, point2) -> {
                    if (point1.y == point2.y) {
                        return point1.x - point2.x;
                    } else if (point1.y < point2.y) {
                        return -1;
                    } else
                        return 1;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
