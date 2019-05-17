package com.codete.regression.screenshot.dynamicareas;

import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
class BoundingBoxCalculator {

    private static final int MIN_NUMBER_OF_POINTS_IN_CLUSTER = 50;
    private static final int MIN_WIDTH_AND_HEIGHT = 10;
    private final ClusterMerger clusterMerger;

    public BoundingBoxCalculator(ClusterMerger clusterMerger) {
        this.clusterMerger = clusterMerger;
    }

    List<IgnoreArea> createBoundingBoxes(Set<Point> points) {
        List<RectangleCluster> clusters = groupPointsIntoClusters(points);
        clusters = clusterMerger.mergeClustersIfTheyContainEachOther(clusters);
        return createIgnoreAreasFromClusters(clusters);
    }

    private List<RectangleCluster> groupPointsIntoClusters(Set<Point> diffPoints) {
        List<RectangleCluster> clusters = new ArrayList<>();
        RectangleCluster rectangleCluster;
        for (Point currentPoint : diffPoints) {
            rectangleCluster = findClusterForPoint(currentPoint, clusters);
            if (rectangleCluster == null) {
                RectangleCluster newCluster = new RectangleCluster(currentPoint);
                clusters.add(newCluster);
            } else {
                rectangleCluster.addPointToTheCluster(currentPoint);
            }
        }
        return clusters;
    }

    private RectangleCluster findClusterForPoint(Point point, List<RectangleCluster> clusters) {
        RectangleCluster foundCluster = null;
        for (RectangleCluster currentCluster : clusters) {
            if (currentCluster.doesPointBelongToTheCluster(point)) {
                foundCluster = currentCluster;
                break;
            }
        }
        return foundCluster;
    }

    private List<IgnoreArea> createIgnoreAreasFromClusters(List<RectangleCluster> clusters) {
        List<IgnoreArea> ignoreAreas = new ArrayList<>();
        for (RectangleCluster cluster : clusters) {
            if (cluster.getPointsCounter() >= MIN_NUMBER_OF_POINTS_IN_CLUSTER
                    && cluster.getHeight() > MIN_WIDTH_AND_HEIGHT && cluster.getWidth() > MIN_WIDTH_AND_HEIGHT) {
                ignoreAreas.add(createIgnoreArea(cluster));
            }
        }
        return ignoreAreas;
    }

    private IgnoreArea createIgnoreArea(RectangleCluster rectangleCluster) {
        IgnoreArea ignoreArea = new IgnoreArea();
        ignoreArea.setXCoordinate(rectangleCluster.getMinX());
        ignoreArea.setYCoordinate(rectangleCluster.getMinY());
        ignoreArea.setHeight(rectangleCluster.getHeight());
        ignoreArea.setWidth(rectangleCluster.getWidth());
        return ignoreArea;
    }

}
