package com.codete.regression.metrics.data;

import lombok.Value;

import java.util.Date;

@Value
public class UserAppMetrics {
    Long appId;
    String appName;
    boolean success;
    long casesCount;
    Date lastRunDate;
}
