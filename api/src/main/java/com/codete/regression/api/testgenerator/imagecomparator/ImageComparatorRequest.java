package com.codete.regression.api.testgenerator.imagecomparator;

import com.codete.regression.api.screenshot.IgnoreAreaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageComparatorRequest {

    private List<IgnoreAreaDto> ignoreAreas;
    private long yOffset;
    private byte[] firstImage;
    private byte[] secondImage;
}
