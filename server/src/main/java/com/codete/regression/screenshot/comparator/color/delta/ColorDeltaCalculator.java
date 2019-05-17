package com.codete.regression.screenshot.comparator.color.delta;

import com.codete.regression.screenshot.comparator.RgbPixel;
import com.codete.regression.screenshot.comparator.color.ColorVector;

public interface ColorDeltaCalculator {

    ColorVector pixelToVector(RgbPixel pixel);

    default double calculate(ColorVector color1, ColorVector color2) {
        double d1 = Math.pow(color1.getC1() - color2.getC1(), 2);
        double d2 = Math.pow(color1.getC2() - color2.getC2(), 2);
        double d3 = Math.pow(color1.getC3() - color2.getC3(), 2);
        double d4 = Math.pow(color1.getC4() - color2.getC4(), 2);
        return Math.sqrt(d1 + d2 + d3 + d4);
    }
}
