package com.codete.regression.screenshot.dynamicareas;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.ImageIOWrapper;
import com.codete.regression.screenshot.comparator.ImageComparator;
import com.codete.regression.screenshot.comparator.ImageComparisonResult;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DynamicAreaDetectorTest {

    private final ImageComparator imageComparator = mock(ImageComparator.class);
    private final ImageIOWrapper imageIOWrapper = mock(ImageIOWrapper.class);
    private final BoundingBoxCalculator boundingBoxCalculator = mock(BoundingBoxCalculator.class);
    private final ArgumentCaptor<Set<Point>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
    private final DynamicAreaDetector dynamicAreaDetector = new DynamicAreaDetector(imageComparator,
            imageIOWrapper, boundingBoxCalculator);

    @Test
    void pointsShouldBeMergedAndSortedBeforeCalculatingBoundingBoxes() {
        List<Screenshot> screenshots = Arrays.asList(mock(Screenshot.class), mock(Screenshot.class), mock(Screenshot.class));
        Point point1 = new Point(1, 0);
        Point point2 = new Point(1, 1);
        Point point3 = new Point(2, 2);
        Point point4 = new Point(1, 0);
        ImageComparisonResult firstResult = mockImageComparisonResultWithDiffPixels(point3, point1);
        ImageComparisonResult secondResult = mockImageComparisonResultWithDiffPixels(point4, point2);
        when(imageIOWrapper.convertToBufferedImage(any())).thenReturn(mock(BufferedImage.class));
        when(imageComparator.compare(any(), any(), any(ComparisonSettings.class))).thenReturn(firstResult).thenReturn(secondResult);

        dynamicAreaDetector.createIgnoreAreasForDynamicElements(mock(ComparisonSettings.class), screenshots);

        verify(boundingBoxCalculator).createBoundingBoxes(argumentCaptor.capture());
        Set<Point> sortedPoints = argumentCaptor.getValue();
        assertThat(sortedPoints, hasSize(3));
        assertThat(sortedPoints, contains(point1, point2, point3));
    }

    private ImageComparisonResult mockImageComparisonResultWithDiffPixels(Point... points) {
        ImageComparisonResult imageComparisonResult = mock(ImageComparisonResult.class);
        when(imageComparisonResult.getDiffPixels()).thenReturn(Arrays.asList(points));
        return imageComparisonResult;
    }

}