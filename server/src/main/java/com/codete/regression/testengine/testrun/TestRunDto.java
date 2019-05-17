package com.codete.regression.testengine.testrun;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
class TestRunDto {

    private String uuid;
    private long testCaseId;
    private boolean passed;
    private long runTimestamp;

    TestRunDto(TestRun testRun) {
        this.uuid = testRun.getUuid();
        this.testCaseId = testRun.getTestCase().getId();
        this.passed = testRun.isPassed();
        this.runTimestamp = testRun.getRunTimestamp().getTime();
    }
}
