package com.codete.regression.testengine.teststep;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class TestStepDto {

    private long id;
    private String stepName;
    private boolean passed;
    private int userDecision;

    TestStepDto(TestStep testStep) {
        this.id = testStep.getId();
        this.stepName = testStep.getStepName();
        this.passed = testStep.getComparisonResult().isPassed();
        this.userDecision = testStep.getUserDecisionNumber();
    }
}
