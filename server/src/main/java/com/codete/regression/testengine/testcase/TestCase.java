package com.codete.regression.testengine.testcase;

import com.codete.regression.testengine.environment.Environment;
import com.codete.regression.testengine.testrun.TestRun;
import com.codete.regression.testengine.teststepconfig.TestStepConfig;
import com.codete.regression.testengine.userapp.UserApp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "test_case_id_seq", sequenceName = "test_case_id_seq", allocationSize = 1)
@Table(name = "test_case", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"test_name", "user_app_id", "environment_id"})})
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_case_id_seq")
    private long id;

    @Column(unique = true)
    private String uuid;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_app_id")
    private UserApp userApp;

    @Column(length = 2083, name = "test_name")
    @NotNull
    private String testName;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "environment_id")
    private Environment environment;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestRun> testRuns = new ArrayList<>();

    @OneToOne
    private TestRun latestTestRun;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestStepConfig> testStepConfigs = new ArrayList<>();

    public TestCase() {
        this.uuid = UUID.randomUUID().toString();
    }

    public TestCase(UserApp userApp, String testName, Environment environment) {
        this.uuid = UUID.randomUUID().toString();
        this.userApp = userApp;
        this.testName = testName;
        this.environment = environment;
    }

    public TestStepConfig getStepConfigForStepName(String stepName) {
        return testStepConfigs
                .stream()
                .filter(testStepConfig -> testStepConfig.getStepName().equals(stepName))
                .findAny()
                .orElse(null);
    }

    public String getUsername() {
        return userApp.getUsername();
    }
}