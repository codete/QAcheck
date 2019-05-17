package com.codete.regression.testengine.teststep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@SequenceGenerator(name = "screenshot_comparison_result_id_seq", sequenceName = "screenshot_comparison_result_id_seq",
        allocationSize = 1)
@Table(name = "screenshot_comparison_result")
public class ComparisonResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screenshot_comparison_result_id_seq")
    private long id;

    private boolean passed;

    private String currentScreenshotPath;

    private String diffScreenshotPath;

    private double diffPercentage;
}