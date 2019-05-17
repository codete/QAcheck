package com.codete.regression.testengine.testgroup.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TestGroupCreateRequest {
    @NotNull
    Long appId;
    String parentUuid;
    String caption;
}
