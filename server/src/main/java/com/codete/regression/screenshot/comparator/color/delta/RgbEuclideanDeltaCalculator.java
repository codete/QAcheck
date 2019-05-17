package com.codete.regression.screenshot.comparator.color.delta;

import com.codete.regression.screenshot.comparator.RgbPixel;
import com.codete.regression.screenshot.comparator.color.ColorVector;

public class RgbEuclideanDeltaCalculator implements ColorDeltaCalculator {

    @Override
    public ColorVector pixelToVector(RgbPixel pixel) {
        return new ColorVector(
                pixel.getRedChannel(),
                pixel.getGreenChannel(),
                pixel.getBlueChannel(),
                pixel.getAlfaChannel()
        );
    }
}
