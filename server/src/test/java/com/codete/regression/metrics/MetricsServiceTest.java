package com.codete.regression.metrics;

import com.codete.regression.metrics.data.UserAppMetrics;
import com.codete.regression.metrics.data.UserAppMetricsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MetricsServiceTest {

    private final MetricsRepository metricsRepository = mock(MetricsRepository.class);

    @InjectMocks
    private MetricsService metricsService;

    @Test
    public void shouldCalculateAppMetrics() throws ParseException {
        long userId = 1L;
        long appId = 1L;
        long successCount = 2L;
        long failsCount = 5L;
        String appName = "testapp";
        Date lastSuccessDate = createDate("11-02-1991");
        Date lastFailDate = createDate("23-02-2019");

        when(metricsRepository.findAppMetricsByUserId(userId)).thenReturn(Arrays.asList(
            new UserAppMetrics(appId, appName, true, successCount, lastSuccessDate),
            new UserAppMetrics(appId, appName, false, failsCount, lastFailDate)
        ));
        List<UserAppMetricsDto> metrics = metricsService.getUserAppMetrics(userId);

        UserAppMetricsDto resultMetrics = UserAppMetricsDto.builder()
                .appName(appName)
                .lastRunDate(lastFailDate)
                .casesSuccessCount(successCount)
                .casesFailsCount(failsCount)
                .build();

        assertThat(metrics.size(), is(1));
        assertThat(metrics.get(0), is(resultMetrics));

        verify(metricsRepository, times(1)).findAppMetricsByUserId(userId);

    }

    private Date createDate(String value) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(value);
    }
}
