package com.codete.regression.screenshot.dynamicareas;

import com.codete.regression.api.screenshot.IgnoreAreaDto;
import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("dynamic-areas")
public class DynamicAreasController {

    private final ScreenshotConcatenator screenshotConcatenator;
    private final DynamicAreaDetector dynamicAreaDetector;

    public DynamicAreasController(ScreenshotConcatenator screenshotConcatenator,
                                  DynamicAreaDetector dynamicAreaDetector) {
        this.screenshotConcatenator = screenshotConcatenator;
        this.dynamicAreaDetector = dynamicAreaDetector;
    }

    @PutMapping("/detect")
    public List<IgnoreAreaDto> runComparison(@RequestBody List<Screenshot> screenshots) {
        List<BufferedImage> concatenatedScreenshots = screenshotConcatenator.concatenateScreenshotParts(screenshots);
        ComparisonSettings comparisonSettings = new ComparisonSettings();
        List<IgnoreArea> ignoreAreas = dynamicAreaDetector.createIgnoreAreasForDynamicElementsBufferedImages(comparisonSettings,
                concatenatedScreenshots);
        return ignoreAreas.stream().map(IgnoreArea::convertToIgnoreAreaDto).collect(Collectors.toList());
    }
}
