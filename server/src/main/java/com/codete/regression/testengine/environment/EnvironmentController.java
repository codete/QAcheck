package com.codete.regression.testengine.environment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/environments")
@RequiredArgsConstructor
public class EnvironmentController {
    private final EnvironmentService environmentService;

    @GetMapping("/{appName}/filters")
    public EnvironmentFiltersDto getUserAppEnvironmentsFilters(@PathVariable("appName") String appName) {
        List<Environment> environments = environmentService.findAllEnvironmentsByAppName(appName);
        return new EnvironmentFiltersDto(environments);
    }

}
