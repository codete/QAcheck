package com.codete.regression.testengine.teststep;

import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.testrun.TestRun;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import com.codete.regression.testengine.userdecision.UserDecision;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@SequenceGenerator(name = "test_step_id_seq", sequenceName = "test_step_id_seq", allocationSize = 1)
@Table(name = "test_step", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"step_name", "test_run_id"})})
public class TestStep {

    @Id
    @GeneratedValue(generator = "test_step_id_seq")
    private long id;

    @NotNull
    @Column(name = "step_name")
    private String stepName;

    @NotNull
    @ManyToOne
    private TestStepConfig testStepConfig;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @NotNull
    @ManyToOne
    private UserDecision userDecision;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ComparisonSettings comparisonSettings;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ComparisonResult comparisonResult;

    public int getUserDecisionNumber() {
        return userDecision.getValue().getNumberRepresentation();
    }

    public String getUsername() {
        return testRun.getUsername();
    }
}
