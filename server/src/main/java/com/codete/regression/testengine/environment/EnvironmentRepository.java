package com.codete.regression.testengine.environment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnvironmentRepository extends JpaRepository<Environment, Long>, JpaSpecificationExecutor<Environment> {

    @Query("SELECT DISTINCT env FROM UserApp app " +
            "INNER JOIN app.testCases c " +
            "INNER JOIN c.environment env " +
            "WHERE app.appName = :appName")
    List<Environment> findAllByAppName(@Param("appName") String appName);
}
