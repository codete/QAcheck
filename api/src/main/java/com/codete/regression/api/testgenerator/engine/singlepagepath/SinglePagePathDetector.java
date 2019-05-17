package com.codete.regression.api.testgenerator.engine.singlepagepath;

import com.codete.regression.api.testgenerator.engine.mouseclick.ClickElementDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class SinglePagePathDetector {

    private final ClickElementDetector clickElementDetector = new ClickElementDetector();
    private final SinglePagePathAnalyzer singlePagePathAnalyzer = new SinglePagePathAnalyzer();

    List<SinglePagePath> findSinglePagePaths(RemoteWebDriver driver) {
        List<String> clickElementDetectors = clickElementDetector.searchElements(driver);
        List<SinglePagePath> singlePagePathsForClickElements = singlePagePathAnalyzer
                .searchPathsForClickElements(driver, clickElementDetectors);
        Set<String> reachedElements = new HashSet<>();
        for (SinglePagePath singlePagePath : singlePagePathsForClickElements) {
            reachedElements.addAll(singlePagePath.getOccurredElements());
        }
        List<SinglePagePath> singlePagePaths = extendSinglePagePaths(driver, singlePagePathsForClickElements, reachedElements);
        singlePagePaths.addAll(singlePagePathsForClickElements);
        return singlePagePaths;
    }

    private List<SinglePagePath> extendSinglePagePaths(RemoteWebDriver driver, List<SinglePagePath> singlePagePaths,
                                                       Set<String> reachedElements) {
        if (singlePagePaths.isEmpty()) {
            return singlePagePaths;
        }
        List<SinglePagePath> newActions = new ArrayList<>();
        for (SinglePagePath singlePagePath : singlePagePaths) {
            reachedElements.addAll(singlePagePath.getOccurredElements());
            newActions.addAll(singlePagePathAnalyzer.searchForNewPaths(driver, singlePagePath, reachedElements));
        }
        newActions.addAll(extendSinglePagePaths(driver, newActions, reachedElements));
        return newActions;
    }
}
