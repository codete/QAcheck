package com.codete.regression.screenshot.comparator.color.delta;

import com.codete.regression.screenshot.comparator.RgbPixel;
import com.codete.regression.screenshot.comparator.color.ColorVector;

/**
 * Converts colors from RGB to CIELAB color space before calculating the difference (euclidean distance).
 * CIELAB was designed to be perceptually uniform with respect to human color vision,
 * meaning that the same amount of numerical change in these values corresponds
 * to about the same amount of visually perceived change.
 */
public class CieLabDeltaCalculator implements ColorDeltaCalculator {
    private final double[] referenceWhiteColorXyz;

    public CieLabDeltaCalculator() {
        this.referenceWhiteColorXyz = convertRgbToXyz(new double[]{1.0, 1.0, 1.0});
    }

    @Override
    public ColorVector pixelToVector(RgbPixel pixel) {
        double[] cielabColor = convertXyzToCieLab(convertRgbToXyz(normalize(pixel)));
        return new ColorVector((int)cielabColor[0], (int)cielabColor[1], (int)cielabColor[2], pixel.getAlfaChannel());
    }

    private double[] normalize(RgbPixel pixel) {
        double r = pixel.getRedChannel() / 255.0;
        double g = pixel.getGreenChannel() / 255.0;
        double b = pixel.getBlueChannel() / 255.0;
        return new double[]{r, g, b};
    }

    private double[] convertRgbToXyz(double[] color) {
        double x = color[0] * 0.4887180 + color[1] * 0.3106803 + color[2] * 0.2006017;
        double y = color[0] * 0.1762044 + color[1] * 0.8129847 + color[2] * 0.0108109;
        double z = color[1] * 0.0102048 + color[2] * 0.9897952;
        return new double[]{x, y, z};
    }

    private double[] convertXyzToCieLab(double[] color) {
        double c1 = f(color[0] / referenceWhiteColorXyz[0]);
        double c2 = f(color[1] / referenceWhiteColorXyz[1]);
        double c3 = f(color[2] / referenceWhiteColorXyz[2]);

        return new double[]{116 * c2 - 16, 500 * (c1 - c2), 200 * (c2 - c3)};
    }

    private double f(double t) {
        return (t > 0.00885645167904) ? Math.pow(t, 1.0 / 3.0) : 70.08333333333263 * t + 0.13793103448276;
    }
}
