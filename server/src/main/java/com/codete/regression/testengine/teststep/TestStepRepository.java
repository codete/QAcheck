package com.codete.regression.testengine.teststep;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface TestStepRepository extends JpaRepository<TestStep, Long> {

    List<TestStep> findAllByTestRunUuid(String uuid);

    Long countByComparisonSettingsIdAndIdNot(Long comparisonSettingsId, Long id);
}
