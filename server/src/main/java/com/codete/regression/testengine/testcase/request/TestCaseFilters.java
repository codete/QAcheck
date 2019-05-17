package com.codete.regression.testengine.testcase.request;

import lombok.Data;

@Data
public class TestCaseFilters {
    private String testName;
    private String os;
    private String browser;
    private Integer viewPortWidth;
    private Integer viewPortHeight;
    private Boolean passed;
    private Long lastRunTimestampFrom;
    private Long lastRunTimestampTo;
}
