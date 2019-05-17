package com.codete.regression.testengine.testrun.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class MultiTestRunRequest {
    @NotBlank
    private String testCaseUuid;

    @Min(1)
    private int limit = 30;

    @Min(0)
    private int page = 0;
}
