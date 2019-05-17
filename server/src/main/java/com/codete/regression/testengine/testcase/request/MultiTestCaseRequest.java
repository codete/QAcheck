package com.codete.regression.testengine.testcase.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class MultiTestCaseRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String appName;

    @Min(1)
    private int limit = 30;

    @Min(0)
    private int page = 0;

    private TestCaseFilters filters = new TestCaseFilters();
}
