package com.codete.regression.screenshot.comparator;

import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class IgnoreAreasProcessorTest {

    private final IgnoreArea ignoreArea = createIgnoreArea(0, 0, 100, 100);

    @Test
    void shouldInitializeIgnoreAreas() {
        Point point = new Point(50, 50);
        IgnoreAreasProcessor ignoreAreasProcessor = new IgnoreAreasProcessor(Collections.singletonList(ignoreArea));

        boolean pixelIgnored = ignoreAreasProcessor.isPixelIgnored(point);

        assertThat("Pixel should be ignored", pixelIgnored, is(true));
    }

    @Test
    void shouldIgnoreOnlyProperPixels() {
        Point point = new Point(150, 150);
        IgnoreAreasProcessor ignoreAreasProcessor = new IgnoreAreasProcessor(Collections.singletonList(ignoreArea));

        boolean pixelIgnored = ignoreAreasProcessor.isPixelIgnored(point);

        assertThat("Pixel should not be ignored", pixelIgnored, is(false));
    }

    private IgnoreArea createIgnoreArea(int x, int y, int width, int height) {
        IgnoreArea ignoreArea = new IgnoreArea();
        ignoreArea.setXCoordinate(x);
        ignoreArea.setYCoordinate(y);
        ignoreArea.setWidth(width);
        ignoreArea.setHeight(height);
        return ignoreArea;
    }

}