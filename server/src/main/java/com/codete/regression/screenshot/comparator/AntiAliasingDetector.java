package com.codete.regression.screenshot.comparator;

import com.codete.regression.screenshot.comparator.color.ColorBitmap;
import com.codete.regression.screenshot.comparator.color.ColorVector;
import com.codete.regression.screenshot.comparator.color.delta.ColorDeltaCalculator;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import org.springframework.stereotype.Service;

import java.awt.Point;

@Service
public class AntiAliasingDetector {

    public boolean isAntiAliasing(
            ColorBitmap colorBitmap1,
            ColorBitmap colorBitmap2,
            Point point,
            ComparisonSettings comparisonSettings,
            ColorDeltaCalculator deltaCalculator
    ) {

        int xShift = comparisonSettings.getHorizontalShift();
        int yShift = comparisonSettings.getVerticalShift();

        if (xShift <= 0 && yShift <= 0) {
            return false;
        }

        ColorVector color1 = colorBitmap1.getColor(point.x, point.y);
        ColorVector color2 = colorBitmap2.getColor(point.x, point.y);

        return (detectAntiAliasing(colorBitmap1, colorBitmap2, point, color1, comparisonSettings, deltaCalculator) &&
                detectAntiAliasing(colorBitmap2, colorBitmap1, point, color2, comparisonSettings, deltaCalculator));
    }

    private boolean detectAntiAliasing(
            ColorBitmap colorBitmap1,
            ColorBitmap colorBitmap2,
            Point point,
            ColorVector color,
            ComparisonSettings comparisonSettings,
            ColorDeltaCalculator deltaCalculator) {

        double deltaThreshold = comparisonSettings.getAllowedDelta();
        int xShift = comparisonSettings.getHorizontalShift();
        int yShift = comparisonSettings.getVerticalShift();

        int width = colorBitmap1.getWidth();
        int height = colorBitmap1.getHeight();

        int xStart = Math.max(point.x - xShift, 0);
        int xEnd = Math.min(point.x + xShift + 1, width);

        int yStart = Math.max(point.y - yShift, 0);
        int yEnd = Math.min(point.y + yShift + 1, height);

        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                if (x != point.x || y != point.y) {
                    ColorVector color1 = colorBitmap1.getColor(x, y);
                    ColorVector color2 = colorBitmap2.getColor(x, y);
                    double localDeltaThreshold = deltaCalculator.calculate(color, color1);
                    double delta = deltaCalculator.calculate(color, color2);
                    if ((Math.abs(localDeltaThreshold - delta) < deltaThreshold) &&  (localDeltaThreshold > deltaThreshold)) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

}
