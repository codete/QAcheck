package com.codete.regression.screenshot.dynamicareas;

import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RectangleClusterTest {

    @Test
    void shouldAddPointToClusterDuringConstruction() {
        Point point = new Point();
        RectangleCluster rectangleCluster = new RectangleCluster(point);

        assertThat(rectangleCluster.getPointsCounter(), is(1));
    }

    @Test
    void shouldReturnTrueIfClusterContainsAnother() {
        RectangleCluster outerCluster = createRectangleCluster(new Point(0, 0), new Point(50, 0),
                new Point(0, 50), new Point(50, 50));
        RectangleCluster innerCluster = createRectangleCluster(new Point(10, 10), new Point(20, 10),
                new Point(10, 50), new Point(20, 50));

        boolean clusterContainsAnother = outerCluster.doesClusterContainAnother(innerCluster);

        assertThat(clusterContainsAnother, is(true));
    }

    @Test
    void shouldReturnFalseIfClusterDoesNotContainAnother() {
        RectangleCluster outerCluster = createRectangleCluster(new Point(0, 0), new Point(50, 0),
                new Point(0, 50), new Point(50, 50));
        RectangleCluster innerCluster = createRectangleCluster(new Point(10, 10), new Point(60, 10),
                new Point(10, 50), new Point(60, 50));

        boolean clusterContainsAnother = outerCluster.doesClusterContainAnother(innerCluster);

        assertThat(clusterContainsAnother, is(false));
    }

    @Test
    void shouldReturnTrueIfPointShouldBeIncludedInTheCluster() {
        RectangleCluster cluster = createRectangleCluster(new Point(0, 0), new Point(50, 0));

        boolean pointBelongsToTheCluster = cluster.doesPointBelongToTheCluster(new Point(100, 0));

        assertThat(pointBelongsToTheCluster, is(true));
    }

    @Test
    void shouldReturnFalseIfPointShouldNotBeIncludedInTheCluster() {
        RectangleCluster cluster = createRectangleCluster(new Point(0, 0), new Point(50, 0));

        boolean pointBelongsToTheCluster = cluster.doesPointBelongToTheCluster(new Point(120, 0));

        assertThat(pointBelongsToTheCluster, is(false));
    }

    private RectangleCluster createRectangleCluster(Point point1, Point... points) {
        RectangleCluster cluster = new RectangleCluster(point1);
        for (Point point : points) {
            cluster.addPointToTheCluster(point);
        }
        return cluster;
    }

}