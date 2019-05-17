package com.codete.regression.screenshot.comparator;

import com.codete.regression.screenshot.comparator.color.ColorBitmap;
import com.codete.regression.screenshot.comparator.color.ColorVector;
import com.codete.regression.screenshot.comparator.color.delta.ColorDeltaCalculator;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

@Service
public class BufferedImageToColorBitmapConverter {

    public ColorBitmap convert(BufferedImage image, ColorDeltaCalculator deltaCalculator) {
        int width = image.getWidth();
        int height = image.getHeight();
        ColorVector[][] colors = new ColorVector[height][width];
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        boolean hasAlphaChannel = image.getAlphaRaster() != null;
        int pixelLength = hasAlphaChannel ? 4 : 3;

        for (int i = 0, y = 0, x = 0; i < pixels.length; i += pixelLength) {
            RgbPixel pixel = hasAlphaChannel ? convertArgbToPixel(pixels, i) : convertRgbToPixel(pixels, i);
            colors[y][x] = deltaCalculator.pixelToVector(pixel);
            x++;
            if (x == width) {
                x = 0;
                y++;
            }
        }

        return new ColorBitmap(width, height, colors);
    }

    private RgbPixel convertRgbToPixel(byte[] pixels, int index) {
        int b = pixels[index] & 0xff;
        int g = pixels[index + 1] & 0xff;
        int r = pixels[index + 2] & 0xff;
        return new RgbPixel(255, r, g, b);
    }

    private RgbPixel convertArgbToPixel(byte[] pixels, int index) {
        int a = pixels[index] & 0xff;
        int b = pixels[index + 1] & 0xff;
        int g = pixels[index + 2] & 0xff;
        int r = pixels[index + 3] & 0xff;
        return new RgbPixel(a, r, g, b);
    }

}
