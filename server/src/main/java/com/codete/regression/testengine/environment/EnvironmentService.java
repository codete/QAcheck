package com.codete.regression.testengine.environment;

import com.codete.regression.api.screenshot.EnvironmentSettings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;

    public EnvironmentService(EnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
    }

    public List<Environment> findAllEnvironmentsByAppName(String appName) {
        return environmentRepository.findAllByAppName(appName);
    }

    @Transactional
    public Environment findByEnvironmentSettingsOrCreateNewOne(EnvironmentSettings environmentSettings) {
        return environmentRepository.findOne(environmentSettingsSpecification(environmentSettings))
                .orElseGet(() -> environmentRepository.save(new Environment(environmentSettings)));
    }

    private Specification<Environment> environmentSettingsSpecification(EnvironmentSettings environmentSettings) {
        return (Specification<Environment>) (root, query, builder) ->
                builder.and(builder.equal(root.get("os"), environmentSettings.getOs()),
                        builder.equal(root.get("browser"), environmentSettings.getBrowser()),
                        builder.equal(root.get("viewPortHeight"), environmentSettings.getViewPortHeight()),
                        builder.equal(root.get("viewPortWidth"), environmentSettings.getViewPortWidth()));
    }
}
