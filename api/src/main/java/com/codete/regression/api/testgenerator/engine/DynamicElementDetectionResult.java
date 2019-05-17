package com.codete.regression.api.testgenerator.engine;

import com.codete.regression.api.testgenerator.imagecomparator.ImageComparatorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DynamicElementDetectionResult {
    private byte[] screenshotBefore;
    private byte[] screenshotAfter;
    private ImageComparatorResponse imageComparatorResponse;

    public byte[] getDiffImage() {
        return imageComparatorResponse.getDiffImage();
    }

    public double getDifference() {
        return imageComparatorResponse.getDifference();
    }
}
