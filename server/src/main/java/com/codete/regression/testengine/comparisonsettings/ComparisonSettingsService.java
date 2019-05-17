package com.codete.regression.testengine.comparisonsettings;

import com.codete.regression.api.testengine.TestRunConfig;
import com.codete.regression.testengine.teststep.TestStep;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import com.codete.regression.testengine.userdecision.UserDecision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum.BASELINE_ACCEPTED;
import static com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum.CURRENT_ACCEPTED;

@Service
public class ComparisonSettingsService {

    private final ComparisonSettingsRepository comparisonSettingsRepository;

    public ComparisonSettingsService(ComparisonSettingsRepository comparisonSettingsRepository) {
        this.comparisonSettingsRepository = comparisonSettingsRepository;
    }

    public ComparisonSettings getComparisonSettings(TestStepConfig testStepConfig,
                                                    TestRunConfig testRunConfig) {
        ComparisonSettings comparisonSettings;
        if (testStepConfig == null) {
            comparisonSettings = new ComparisonSettings();
        } else {
            comparisonSettings = getComparisonSettingsBasedOnTestStepConfig(testStepConfig, testRunConfig);
        }
        comparisonSettings.setAllowedDifferencePercentage(testRunConfig.getAllowedDifferencePercentage());
        comparisonSettings.setAllowedDelta(testRunConfig.getAllowedDelta());
        comparisonSettings.setHorizontalShift(testRunConfig.getHorizontalShift());
        comparisonSettings.setVerticalShift(testRunConfig.getVerticalShift());
        comparisonSettings.setShowDetectedShift(testRunConfig.isShowDetectedShift());
        comparisonSettings.setPerceptualMode(testRunConfig.isPerceptualMode());
        return comparisonSettings;
    }

    @Transactional
    public void applyUserDecision(ComparisonSettings baseComparisonSettings,
                                  UserDecision.UserDecisionEnum userDecisionEnum,
                                  TestStep testStep) {
        if (userDecisionEnum == CURRENT_ACCEPTED) {
            baseComparisonSettings.setBaselineScreenshotPath(testStep.getComparisonResult().getCurrentScreenshotPath());
            comparisonSettingsRepository.save(baseComparisonSettings);
        } else if (userDecisionEnum == BASELINE_ACCEPTED) {
            ComparisonSettings currentComparisonSettings = testStep.getComparisonSettings();
            if (!baseComparisonSettings.getBaselineScreenshotPath()
                    .equals(currentComparisonSettings.getBaselineScreenshotPath())) {
                baseComparisonSettings.setBaselineScreenshotPath(currentComparisonSettings.getBaselineScreenshotPath());
                comparisonSettingsRepository.save(baseComparisonSettings);
            }
        }
    }

    @Transactional
    public void mergeComparisonSettings(ComparisonSettings comparisonSettings,
                                        ComparisonSettingsDto comparisonSettingsDto) {
        List<IgnoreArea> newIgnoreAreas = comparisonSettingsDto
                .getIgnoreAreas()
                .stream()
                .map(ignoreAreaDto -> new IgnoreArea(ignoreAreaDto, comparisonSettings))
                .collect(Collectors.toList());
        comparisonSettings.getIgnoreAreas().clear();
        comparisonSettings.getIgnoreAreas().addAll(newIgnoreAreas);
        comparisonSettings.setAllowedDifferencePercentage(comparisonSettingsDto.getAllowedDifferencePercentage());
        comparisonSettings.setAllowedDelta(comparisonSettingsDto.getAllowedDelta());
        comparisonSettings.setHorizontalShift(comparisonSettingsDto.getHorizontalShift());
        comparisonSettings.setVerticalShift(comparisonSettingsDto.getVerticalShift());
        comparisonSettings.setShowDetectedShift(comparisonSettingsDto.isShowDetectedShift());
        comparisonSettings.setPerceptualMode(comparisonSettingsDto.isPerceptualMode());
        comparisonSettingsRepository.save(comparisonSettings);
    }

    private ComparisonSettings getComparisonSettingsBasedOnTestStepConfig(TestStepConfig testStepConfig,
                                                                          TestRunConfig testRunConfig) {
        ComparisonSettings comparisonSettings = testStepConfig.getComparisonSettings();
        if (isSettingsDifferentFromTestRunConfig(comparisonSettings, testRunConfig)) {
            comparisonSettings = new ComparisonSettings(comparisonSettings);
        }
        return comparisonSettings;
    }

    private boolean isSettingsDifferentFromTestRunConfig(ComparisonSettings settings, TestRunConfig config) {

        return (config.getAllowedDifferencePercentage() != settings.getAllowedDifferencePercentage()) ||
                (config.getAllowedDelta() != settings.getAllowedDelta()) ||
                (config.getHorizontalShift() != settings.getHorizontalShift()) ||
                (config.getVerticalShift() != settings.getVerticalShift()) ||
                (config.isPerceptualMode() != settings.isPerceptualMode()) ||
                (config.isShowDetectedShift() != settings.isShowDetectedShift());
    }

}
