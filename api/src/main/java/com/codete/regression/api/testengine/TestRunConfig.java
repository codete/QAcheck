package com.codete.regression.api.testengine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestRunConfig {

    private double allowedDifferencePercentage;
    private double allowedDelta = 0.0;
    private int horizontalShift = 0;
    private int verticalShift = 0;
    private boolean showDetectedShift = false;
    private boolean perceptualMode = false;

}
