package com.codete.regression.testengine.comparisonsettings;

import com.codete.regression.testengine.teststep.TestStep;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "comparison_settings_id_seq", sequenceName = "comparison_settings_id_seq",
        allocationSize = 1)
@Table(name = "comparison_settings")
public class ComparisonSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comparison_settings_id_seq")
    private Long id;

    private String baselineScreenshotPath;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "comparisonSettings")
    private List<IgnoreArea> ignoreAreas = new ArrayList<>();

    @OneToOne(mappedBy = "comparisonSettings")
    private TestStepConfig testStepConfig;

    @OneToMany(mappedBy = "comparisonSettings")
    private List<TestStep> testSteps = new ArrayList<>();

    private double allowedDifferencePercentage;

    private double allowedDelta = 0.0;

    private int horizontalShift = 0;

    private int verticalShift = 0;

    private boolean showDetectedShift = false;

    private boolean perceptualMode = false;

    public ComparisonSettings() {
    }

    public ComparisonSettings(ComparisonSettings comparisonSettings) {
        this.setBaselineScreenshotPath(comparisonSettings.getBaselineScreenshotPath());
        List<IgnoreArea> copiedIgnoreAreas = comparisonSettings.getIgnoreAreas()
                .stream()
                .map(ignoreArea -> new IgnoreArea(ignoreArea, this))
                .collect(Collectors.toList());
        this.setIgnoreAreas(copiedIgnoreAreas);
        this.allowedDifferencePercentage = comparisonSettings.getAllowedDifferencePercentage();
        this.allowedDelta = comparisonSettings.getAllowedDelta();
        this.horizontalShift = comparisonSettings.getHorizontalShift();
        this.verticalShift = comparisonSettings.getVerticalShift();
        this.perceptualMode = comparisonSettings.isPerceptualMode();
        this.showDetectedShift = comparisonSettings.isShowDetectedShift();
    }

}
