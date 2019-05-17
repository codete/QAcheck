package com.codete.regression.testengine.teststepdetails;

import com.codete.regression.screenshot.ScreenshotsDto;
import com.codete.regression.screenshot.ScreenshotsDtoFactory;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsDto;
import com.codete.regression.testengine.teststep.TestStep;
import org.springframework.stereotype.Service;

@Service
public class TestStepDetailsFactory {
    private final ScreenshotsDtoFactory screenshotsDtoFactory;

    public TestStepDetailsFactory(ScreenshotsDtoFactory screenshotsDtoFactory) {
        this.screenshotsDtoFactory = screenshotsDtoFactory;
    }

    public TestStepDetailsDto buildTestStepDetailsDto(TestStep testStep){
        ComparisonSettings settings = testStep.getComparisonSettings();
        ComparisonSettingsDto settingsDto = new ComparisonSettingsDto(settings);
        ScreenshotsDto screenshotsDto = screenshotsDtoFactory.createScreenshotsDto(testStep);
        return new TestStepDetailsDto(settingsDto, screenshotsDto);
    }
}
