package com.codete.regression.screenshot.comparator.color.delta;

import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import org.springframework.stereotype.Service;

@Service
public class ColorDeltaCalculatorFactory {

    public ColorDeltaCalculator create(ComparisonSettings comparisonSettings) {
        if (comparisonSettings.isPerceptualMode()) {
            return new CieLabDeltaCalculator();
        }

        return new RgbEuclideanDeltaCalculator();
    }
}
