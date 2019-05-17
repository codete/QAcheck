package com.codete.regression.screenshot.comparator.color;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ColorBitmap {
    @Getter
    private final int width;

    @Getter
    private final int height;

    private final ColorVector[][] colors;

    public ColorVector getColor(int x, int y) {
        return colors[y][x];
    }

}
