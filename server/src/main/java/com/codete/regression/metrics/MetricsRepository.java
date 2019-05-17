package com.codete.regression.metrics;

import com.codete.regression.metrics.data.UserAppMetrics;
import com.codete.regression.testengine.userapp.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetricsRepository extends JpaRepository<UserApp, Long> {

    @Query("SELECT new com.codete.regression.metrics.data.UserAppMetrics(app.id, app.appName, r.passed, COUNT(c.uuid), MAX(r.runTimestamp)) " +
            "FROM UserApp app " +
            "INNER JOIN app.testCases c " +
            "INNER JOIN c.latestTestRun r " +
            "WHERE app.user.id = :userId " +
            "GROUP BY app.id, r.passed")
    List<UserAppMetrics> findAppMetricsByUserId(@Param("userId") Long userId);
}