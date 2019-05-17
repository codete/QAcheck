package com.codete.regression.screenshot.dynamicareas;

import lombok.Getter;

import java.awt.Point;

class RectangleCluster {

    private static final int SQUARED_MAX_DISTANCE_FROM_CLUSTER = 2500;
    private int centerX;
    private int centerY;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    @Getter
    private int minX = Integer.MAX_VALUE;
    @Getter
    private int minY = Integer.MAX_VALUE;
    @Getter
    private int width;
    @Getter
    private int height;
    @Getter
    private int pointsCounter;

    RectangleCluster(Point point) {
        addPointToTheCluster(point);
    }

    boolean doesClusterContainAnother(RectangleCluster rectangleCluster) {
        return rectangleCluster.getMinX() >= minX && rectangleCluster.getMinY() >= minY
                && (rectangleCluster.getMinX() + rectangleCluster.getWidth()) <= (minX + width)
                && (rectangleCluster.getMinY() + rectangleCluster.getHeight()) <= (minY + height);
    }

    boolean doesPointBelongToTheCluster(Point point) {
        double dx = Math.max(Math.abs(centerX - point.x) - width / 2, 0);
        double dy = Math.max(Math.abs(centerY - point.y) - height / 2, 0);
        return (dx * dx + dy * dy) <= SQUARED_MAX_DISTANCE_FROM_CLUSTER;
    }

    void addPointToTheCluster(Point point) {
        if (minX > point.x) {
            minX = point.x;
        }
        if (minY > point.y) {
            minY = point.y;
        }
        if (maxX < point.x) {
            maxX = point.x;
        }
        if (maxY < point.y) {
            maxY = point.y;
        }
        width = calculateLength(maxX, minX);
        height = calculateLength(maxY, minY);
        centerX = minX + width / 2;
        centerY = minY + height / 2;
        pointsCounter++;
    }

    private int calculateLength(int maxCoordinate, int minCoordinate) {
        int length = 1;
        if (maxCoordinate != minCoordinate) {
            length = maxCoordinate - minCoordinate;
        }
        return length;
    }
}
