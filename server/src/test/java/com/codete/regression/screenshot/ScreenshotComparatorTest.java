package com.codete.regression.screenshot;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.comparator.ImageComparator;
import com.codete.regression.screenshot.comparator.ImageComparisonResult;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.teststep.ComparisonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.image.BufferedImage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class ScreenshotComparatorTest {

    private final ImageIOWrapper imageIOWrapper = mock(ImageIOWrapper.class);
    private final ScreenshotStorage screenshotStorage = mock(ScreenshotStorage.class);
    private final ImageComparator imageComparator = mock(ImageComparator.class);
    private final BufferedImage currentBufferedImage = mock(BufferedImage.class);
    private final ScreenshotBufferedImage currentImage = mock(ScreenshotBufferedImage.class);
    private final ScreenshotComparator screenshotComparator = new ScreenshotComparator(imageIOWrapper, screenshotStorage,
            imageComparator);

    @BeforeEach
    void setUp() {
        when(currentImage.getScreenshot()).thenReturn(currentBufferedImage);
        when(screenshotStorage.saveScreenshot(any(), any(), any())).thenReturn(currentImage);
    }

    @Test
    void shouldInitBaselineWhenItDoesNotExist() {
        ComparisonResult comparisonResult = screenshotComparator.saveAndCompareScreenshotWithTheBaseline(
                new ComparisonSettings(), "screenshotLocation", mock(Screenshot.class));

        assertThat("Test should pass when it is run for the first time", comparisonResult.isPassed(),
                is(true));
        verifyZeroInteractions(imageComparator);
    }

    @Test
    void shouldCallImageComparatorWhenComparisonSettingsExist() {
        String baselineScreenshotPath = "baseline";
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);
        BufferedImage baselineBufferedImage = mock(BufferedImage.class);
        ScreenshotBufferedImage baselineImage = mock(ScreenshotBufferedImage.class);
        ImageComparisonResult imageComparisonResult = mock(ImageComparisonResult.class);

        when(baselineImage.getScreenshot()).thenReturn(baselineBufferedImage);
        when(comparisonSettings.getBaselineScreenshotPath()).thenReturn(baselineScreenshotPath);
        when(screenshotStorage.readScreenshot(baselineScreenshotPath)).thenReturn(baselineImage);
        when(imageComparator.compare(baselineBufferedImage, currentBufferedImage, comparisonSettings)).thenReturn(imageComparisonResult);
        when(imageComparisonResult.isPassed()).thenReturn(true);

        ComparisonResult comparisonResult = screenshotComparator.saveAndCompareScreenshotWithTheBaseline(
                comparisonSettings, "screenshotLocation", mock(Screenshot.class));

        assertThat("Test should pass when there is no difference between images.", comparisonResult.isPassed(),
                is(true));
    }
}
