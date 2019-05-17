package com.codete.regression.testengine.teststepconfig;

import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.testcase.TestCase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "test_step_config_id_seq", sequenceName = "test_step_config_id_seq", allocationSize = 1)
@Table(name = "test_step_config", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"step_name", "test_case_id"})})
public class TestStepConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_step_config_id_seq")
    private long id;

    @NotNull
    @Column(name = "step_name")
    private String stepName;

    @OneToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private ComparisonSettings comparisonSettings;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "test_case_id")
    private TestCase testCase;
}
