package com.codete.regression.testengine.comparisonsettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface ComparisonSettingsRepository extends JpaRepository<ComparisonSettings, Long> {

    @Query("select ss from ComparisonSettings ss " +
            "LEFT JOIN ss.testSteps s " +
            "LEFT JOIN ss.testStepConfig sc " +
            "WHERE s.id IS NULL AND sc.id IS NULL")
    Stream<ComparisonSettings> findAllOrphans();


    long countByBaselineScreenshotPath(String baselineScreenshotPath);
}
