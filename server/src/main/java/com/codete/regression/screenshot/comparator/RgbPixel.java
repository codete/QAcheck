package com.codete.regression.screenshot.comparator;

import lombok.Value;

/**
 * This class represents RGB pixel coded into int.
 * 8 bits for every color(b,g,r from younger bits), and 8 for alfa channel (transparency).
 * To be able to perform operations on colors, channels should be extracted first using bit operations.
 */
@Value
public class RgbPixel {
    int alfaChannel;
    int redChannel;
    int greenChannel;
    int blueChannel;

    public RgbPixel(int rgbInt) {
        this.alfaChannel = (rgbInt >> 24) & 0xff;
        this.redChannel = (rgbInt >> 16) & 0xff;
        this.greenChannel = (rgbInt >> 8) & 0xff;
        this.blueChannel = rgbInt & 0xff;
    }

    public RgbPixel(int alfaChannel, int redChannel, int greenChannel, int blueChannel) {
        this.alfaChannel = alfaChannel;
        this.redChannel = redChannel;
        this.greenChannel = greenChannel;
        this.blueChannel = blueChannel;
    }

    public int toRgbInt() {
        return ((alfaChannel << 24) | (redChannel << 16) | (greenChannel << 8) | blueChannel);
    }
}
