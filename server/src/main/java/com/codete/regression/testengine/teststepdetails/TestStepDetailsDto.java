package com.codete.regression.testengine.teststepdetails;

import com.codete.regression.screenshot.ScreenshotsDto;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TestStepDetailsDto {
    private final ComparisonSettingsDto comparisonSettings;
    private final ScreenshotsDto screenshots;
}
