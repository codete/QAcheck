package com.codete.regression.api.testengine;

import com.codete.regression.api.screenshot.EnvironmentSettings;
import com.codete.regression.api.screenshot.Screenshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.codete.regression.api.testengine.TestRunPropertyValidator.APP_NAME_MAX_LENGTH;
import static com.codete.regression.api.testengine.TestRunPropertyValidator.APP_NAME_MIN_LENGTH;
import static com.codete.regression.api.testengine.TestRunPropertyValidator.TEST_NAME_MAX_LENGTH;
import static com.codete.regression.api.testengine.TestRunPropertyValidator.TEST_NAME_MIN_LENGTH;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TestRunRequest {

    @NotNull
    @Size(min = APP_NAME_MIN_LENGTH, max = APP_NAME_MAX_LENGTH, message = "App name length should be between 5 - 100.")
    private String appName;

    @NotNull
    @Size(min = TEST_NAME_MIN_LENGTH, max = TEST_NAME_MAX_LENGTH, message = "Test name length should be between 5 - 2083.")
    private String testName;

    private EnvironmentSettings environmentSettings;

    @NotNull
    private List<Screenshot> screenshots;

    private boolean detectDynamicElements;

    private TestRunConfig testRunConfig;
}
