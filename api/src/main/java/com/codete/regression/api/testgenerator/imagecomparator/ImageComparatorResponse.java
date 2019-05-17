package com.codete.regression.api.testgenerator.imagecomparator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ImageComparatorResponse {

    private double difference;
    private byte[] diffImage;
}
