package com.codete.regression.testengine.testcase;

import com.codete.regression.api.screenshot.EnvironmentSettings;
import com.codete.regression.testengine.testrun.TestRun;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
class TestCaseDto {

    private String uuid;
    private long userAppId;
    private boolean passed;
    private String testName;
    private EnvironmentSettings environment;
    private Long lastRunTimestamp;

    TestCaseDto(TestCase testCase) {
        this.uuid = testCase.getUuid();
        this.userAppId = testCase.getUserApp().getId();
        this.testName = testCase.getTestName();
        this.environment = testCase.getEnvironment().createEnvironmentSettings();

        TestRun latestTestRun = testCase.getLatestTestRun();
        if (latestTestRun != null) {
            this.passed = latestTestRun.isPassed();
            this.lastRunTimestamp = latestTestRun.getRunTimestamp().getTime();
        } else {
            this.passed = true;
            this.lastRunTimestamp = 0L;
        }
    }
}
