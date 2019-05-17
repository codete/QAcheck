package com.codete.regression.testengine.testgroup.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddTestCaseRequest {
    @NotBlank
    String testCaseUuid;
}
