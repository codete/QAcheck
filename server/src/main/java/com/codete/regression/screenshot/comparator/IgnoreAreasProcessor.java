package com.codete.regression.screenshot.comparator;

import com.codete.regression.testengine.comparisonsettings.IgnoreArea;

import java.awt.Point;
import java.util.List;

public class IgnoreAreasProcessor {

    private final List<IgnoreArea> ignoreAreas;
    private final long yOffset;

    public IgnoreAreasProcessor(List<IgnoreArea> ignoreAreas) {
        this(ignoreAreas, 0);
    }

    public IgnoreAreasProcessor(List<IgnoreArea> ignoreAreas, long yOffset) {
        this.ignoreAreas = ignoreAreas;
        this.yOffset = yOffset;
    }

    public boolean isPixelIgnored(Point point) {
        for (IgnoreArea field : ignoreAreas) {
            if (pixelIgnored(field, point))
                return true;
        }
        return false;
    }

    private boolean pixelIgnored(IgnoreArea ignoreArea, Point pixel) {
        return pixel.getX() >= ignoreArea.getXCoordinate() &&
                pixel.getX() <= ignoreArea.getXCoordinate() + ignoreArea.getWidth() &&
                pixel.getY() >= ignoreArea.getYCoordinate() - yOffset &&
                pixel.getY() <= ignoreArea.getYCoordinate() + ignoreArea.getHeight() - yOffset;
    }
}
