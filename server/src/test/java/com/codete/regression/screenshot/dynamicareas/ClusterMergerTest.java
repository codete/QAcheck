package com.codete.regression.screenshot.dynamicareas;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClusterMergerTest {

    private final ClusterMerger clusterMerger = new ClusterMerger();

    @Test
    void shouldNotIncludeClustersIfTheyBelongToAnotherClusters() {
        RectangleCluster outerCluster = mock(RectangleCluster.class);
        RectangleCluster innerCluster = mock(RectangleCluster.class);
        when(outerCluster.doesClusterContainAnother(innerCluster)).thenReturn(true);

        List<RectangleCluster> mergedClusters =
                clusterMerger.mergeClustersIfTheyContainEachOther(Arrays.asList(outerCluster, innerCluster));

        assertThat(mergedClusters, contains(outerCluster));
    }

}