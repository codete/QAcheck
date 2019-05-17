package com.codete.regression.testengine.comparisonsettings;

import com.codete.regression.api.screenshot.IgnoreAreaDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
@Getter
@Setter
public class ComparisonSettingsDto {

    private List<IgnoreAreaDto> ignoreAreas;

    @Min(0)
    private double allowedDifferencePercentage;

    @Min(0)
    private double allowedDelta;

    @Min(0)
    private int horizontalShift;

    @Min(0)
    private int verticalShift;

    private boolean showDetectedShift;

    private boolean perceptualMode;

    public ComparisonSettingsDto(ComparisonSettings comparisonSettings) {
        this.allowedDifferencePercentage = comparisonSettings.getAllowedDifferencePercentage();
        this.allowedDelta = comparisonSettings.getAllowedDelta();
        this.horizontalShift = comparisonSettings.getHorizontalShift();
        this.verticalShift = comparisonSettings.getVerticalShift();
        this.showDetectedShift = comparisonSettings.isShowDetectedShift();
        this.perceptualMode = comparisonSettings.isPerceptualMode();
        this.ignoreAreas = comparisonSettings
                .getIgnoreAreas()
                .stream()
                .map(IgnoreArea::convertToIgnoreAreaDto)
                .collect(toList());
    }
}
