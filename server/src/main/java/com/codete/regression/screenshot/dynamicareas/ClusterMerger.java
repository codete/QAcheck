package com.codete.regression.screenshot.dynamicareas;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class ClusterMerger {

    List<RectangleCluster> mergeClustersIfTheyContainEachOther(List<RectangleCluster> clusters) {
        List<RectangleCluster> mergedClusters = new ArrayList<>();
        for (RectangleCluster cluster : clusters) {
            if (!doesClusterBelongToAnotherCluster(cluster, clusters)) {
                mergedClusters.add(cluster);
            }
        }
        return mergedClusters;
    }

    private boolean doesClusterBelongToAnotherCluster(RectangleCluster currentCluster,
                                                      List<RectangleCluster> clusters) {
        for (RectangleCluster cluster : clusters) {
            if (!cluster.equals(currentCluster) && cluster.doesClusterContainAnother(currentCluster)) {
                return true;
            }
        }
        return false;
    }
}
