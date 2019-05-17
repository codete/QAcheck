package com.codete.regression.screenshot.comparator;

import com.codete.regression.screenshot.comparator.color.delta.ColorDeltaCalculatorFactory;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
class ImageComparatorTest {

    private final ImageComparator imageComparator = new ImageComparator(
            new ColorDeltaCalculatorFactory(),
            new BufferedImageToColorBitmapConverter(),
            new AntiAliasingDetector()
    );

    @Test
    void shouldSeeNoDifferenceBetweenTwoExactImages() throws Exception {
        ImageComparisonResult result = compareForResult("images/Lenna50.jpg",
                "images/Lenna50.jpg", new ComparisonSettings());

        assertThat(result.getDifference(), is(0.0d));
    }

    @Test
    void shouldNotCalculateDifferenceOnIgnoredFields() throws Exception {
        IgnoreArea ignoreArea = createIgnoreArea(0, 0, 512, 512);
        ComparisonSettings comparisonSettings = new ComparisonSettings();
        comparisonSettings.setIgnoreAreas(Collections.singletonList(ignoreArea));
        ImageComparisonResult result = compareForResult("images/Lenna50.jpg",
                "images/Lenna100.jpg", comparisonSettings);

        assertThat(result.getDifference(), is(0.0d));
    }

    @Test
    void shouldSeeDifferenceOnTwoDifferentImages() throws Exception {
        ImageComparisonResult result = compareForResult("images/Lenna50.jpg",
                "images/Lenna100.jpg", new ComparisonSettings());

        assertThat(result.getDifference(), is(99.334716796875d));
    }

    @Test
    void shouldDiscriminateOnSlightlyDifferentColors() throws Exception {
        ImageComparisonResult result = compareForResult("images/codete_baseline.png",
                "images/codete_actual.png", new ComparisonSettings());

        assertThat(result.getDifference(), is(4.349205218916794d));
    }


    @Test
    void shouldSeeNoDifferenceBetweenImagesWithDifferentAntiAliasing() throws Exception {
        ComparisonSettings comparisonSettings = new ComparisonSettings();
        comparisonSettings.setAllowedDelta(20.0);
        comparisonSettings.setPerceptualMode(true);
        comparisonSettings.setHorizontalShift(2);
        comparisonSettings.setVerticalShift(2);
        comparisonSettings.setShowDetectedShift(true);

        ImageComparisonResult result = compareForResult("images/antialiasing/antialiased.png",
                "images/antialiasing/aliased.png", comparisonSettings);

        assertThat(result.getDifference(), is(0.0));
    }

    @Test
    void shouldSeeNoDifferenceBetweenSameImagesWithDifferentBrightnessInPerceptualMode() throws Exception {
        ComparisonSettings comparisonSettings = new ComparisonSettings();
        comparisonSettings.setAllowedDelta(15.0);
        comparisonSettings.setPerceptualMode(true);

        ImageComparisonResult result = compareForResult("images/perceptual/original.png",
                "images/perceptual/darker.png", comparisonSettings);

        assertThat(result.getDifference(), is(0.0));
    }

    @Test
    void shouldSeeDifferenceBetweenDifferentImagesInPerceptualMode() throws Exception {
        ComparisonSettings comparisonSettings = new ComparisonSettings();
        comparisonSettings.setAllowedDelta(15.0);
        comparisonSettings.setPerceptualMode(true);

        ImageComparisonResult result = compareForResult("images/perceptual/original.png",
                "images/perceptual/darker_modified.png", comparisonSettings);

        assertThat(result.getDifference(), is(0.016562143961588543));
    }

    private IgnoreArea createIgnoreArea(int x, int y, int width, int height) {
        IgnoreArea ignoreArea = new IgnoreArea();
        ignoreArea.setXCoordinate(x);
        ignoreArea.setYCoordinate(y);
        ignoreArea.setWidth(width);
        ignoreArea.setHeight(height);
        return ignoreArea;
    }

    private ImageComparisonResult compareForResult(String baselineFilename, String actualFilename,
                                                   ComparisonSettings comparisonSettings)
            throws URISyntaxException, IOException {
        File base = new File(getClass().getClassLoader().getResource(baselineFilename).toURI());
        File actual = new File(getClass().getClassLoader().getResource(actualFilename).toURI());
        return imageComparator.compare(ImageIO.read(base), ImageIO.read(actual), comparisonSettings);
    }
}