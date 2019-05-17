package com.codete.regression.api.testgenerator.engine.singlepagepath;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedList;

@Setter
@Getter
class SinglePagePath {

    private final LinkedList<String> clickElementsPath;
    private final Collection<String> occurredElements;

    SinglePagePath(String clickElement, Collection<String> occurredElements) {
        clickElementsPath = new LinkedList<>();
        clickElementsPath.add(clickElement);
        this.occurredElements = occurredElements;
    }

    SinglePagePath(LinkedList<String> clickElementsPath, Collection<String> occurredElements) {
        this.clickElementsPath = clickElementsPath;
        this.occurredElements = occurredElements;
    }
}
