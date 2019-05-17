package com.codete.regression.screenshot.dynamicareas;

import com.codete.regression.testengine.comparisonsettings.IgnoreArea;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

class IgnoreAreaPainter {

    static BufferedImage createImageWithIgnoreAreas(int imageWidth, int imageHeight, List<IgnoreArea> ignoreAreas) {
        BufferedImage diffImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        addWhiteBackgroundToImage(diffImage);
        for (IgnoreArea ignoreArea : ignoreAreas) {
            long xCoordinate = ignoreArea.getXCoordinate();
            long yCoordinate = ignoreArea.getYCoordinate();
            long ignoreAreaWidth = ignoreArea.getWidth();
            long ignoreAreaHeight = ignoreArea.getHeight();
            for (long x = xCoordinate; x < xCoordinate + ignoreAreaWidth; x++) {
                for (long y = yCoordinate; y < yCoordinate + ignoreAreaHeight; y++) {
                    diffImage.setRGB((int) x, (int) y, 0);
                }
            }
        }
        return diffImage;
    }

    private static void addWhiteBackgroundToImage(BufferedImage diffImage) {
        Graphics2D graphics = diffImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, diffImage.getWidth(), diffImage.getHeight());
        graphics.dispose();
    }

}
