package com.codete.regression.testengine.environment;

import lombok.Value;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Value
public class EnvironmentFiltersDto {
    Set<String> os;
    Set<String> browsers;
    Set<Integer> viewPortWidths;
    Set<Integer> viewPortHeights;

    public EnvironmentFiltersDto(List<Environment> environments) {
        this.os = environments.stream().map(Environment::getOs).collect(Collectors.toCollection(TreeSet::new));
        this.browsers = environments.stream().map(Environment::getBrowser).collect(Collectors.toCollection(TreeSet::new));
        this.viewPortWidths = environments.stream().map(Environment::getViewPortWidth).collect(Collectors.toCollection(TreeSet::new));
        this.viewPortHeights = environments.stream().map(Environment::getViewPortHeight).collect(Collectors.toCollection(TreeSet::new));
    }
}
