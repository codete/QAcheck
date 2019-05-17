package com.codete.regression.metrics.data;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class UserAppMetricsDto {
    String appName;
    long casesSuccessCount;
    long casesFailsCount;
    Date lastRunDate;
}
