package com.codete.regression.screenshot.comparator;

import com.codete.regression.api.testgenerator.imagecomparator.ImageComparatorRequest;
import com.codete.regression.api.testgenerator.imagecomparator.ImageComparatorResponse;
import com.codete.regression.screenshot.ImageIOWrapper;
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
@RequestMapping("image-comparator")
public class ImageComparatorController {

    private final ImageComparator imageComparator;
    private final ImageIOWrapper imageIOWrapper;

    public ImageComparatorController(ImageComparator imageComparator, ImageIOWrapper imageIOWrapper) {
        this.imageComparator = imageComparator;
        this.imageIOWrapper = imageIOWrapper;
    }

    @PutMapping("/run-comparison")
    public ImageComparatorResponse runComparison(@RequestBody ImageComparatorRequest request) {
        ComparisonSettings comparisonSettings = createComparisonSettings(request);
        BufferedImage firstImage = imageIOWrapper.convertToBufferedImage(request.getFirstImage());
        BufferedImage secondImage = imageIOWrapper.convertToBufferedImage(request.getSecondImage());
        IgnoreAreasProcessor ignoreAreasProcessor = new IgnoreAreasProcessor(comparisonSettings.getIgnoreAreas(),
                request.getYOffset());
        ImageComparisonResult imageComparisonResult = imageComparator.compare(firstImage, secondImage,
                comparisonSettings, ignoreAreasProcessor);
        byte[] diffImage = imageIOWrapper.convertToBytesArray(imageComparisonResult.getDiffImage());
        return new ImageComparatorResponse(imageComparisonResult.getDifference(), diffImage);
    }

    private ComparisonSettings createComparisonSettings(ImageComparatorRequest request) {
        ComparisonSettings comparisonSettings = new ComparisonSettings();
        List<IgnoreArea> ignoreAreas = request.getIgnoreAreas()
                .stream()
                .map(ignoreAreaDto -> new IgnoreArea(ignoreAreaDto, comparisonSettings))
                .collect(Collectors.toList());
        comparisonSettings.setIgnoreAreas(ignoreAreas);
        return comparisonSettings;
    }

}
