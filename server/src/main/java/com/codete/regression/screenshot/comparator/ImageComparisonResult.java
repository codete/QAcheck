package com.codete.regression.screenshot.comparator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

@AllArgsConstructor
@Getter
public class ImageComparisonResult {
    private BufferedImage diffImage;
    private List<Point> diffPixels;
    private double difference;
    private boolean passed;
}
