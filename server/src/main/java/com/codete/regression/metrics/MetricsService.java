package com.codete.regression.metrics;

import com.codete.regression.metrics.data.UserAppMetrics;
import com.codete.regression.metrics.data.UserAppMetricsDto;
import com.codete.regression.metrics.data.UserAppMetricsDto.UserAppMetricsDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MetricsRepository metricsRepository;

    @Transactional
    public List<UserAppMetricsDto> getUserAppMetrics(Long userId) {
        Map<Long, List<UserAppMetrics>> metricsByAppId = metricsRepository.findAppMetricsByUserId(userId)
                .stream()
                .collect(Collectors.groupingBy(UserAppMetrics::getAppId));

        List<UserAppMetricsDto> results = new ArrayList<>();
        metricsByAppId.forEach((appId, metricsByStatus) -> {
            results.add(aggregateMetrics(metricsByStatus));
        });

        return results;
    }

    private UserAppMetricsDto aggregateMetrics(List<UserAppMetrics> metricsByStatus) {
        UserAppMetricsDtoBuilder builder = UserAppMetricsDto.builder();
        metricsByStatus.forEach(metrics -> {
            builder.appName(metrics.getAppName());
            if (metrics.isSuccess()) {
                builder.casesSuccessCount(metrics.getCasesCount());
            } else {
                builder.casesFailsCount(metrics.getCasesCount());
            }
        });

        Date maxDate = metricsByStatus.stream()
                .map(UserAppMetrics::getLastRunDate)
                .max(Date::compareTo)
                .orElse(null);

        builder.lastRunDate(maxDate);
        return builder.build();
    }
}
