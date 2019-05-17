package com.codete.regression.testengine.testrun;

import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.teststep.ComparisonResult;
import com.codete.regression.testengine.teststep.TestStep;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "test_run_id_seq", sequenceName = "test_run_id_seq", allocationSize = 1)
@Table(name = "test_run")
public class TestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_run_id_seq")
    private long id;

    @Column(unique = true)
    private String uuid;

    private boolean passed;

    @ManyToOne
    @NotNull
    private TestCase testCase;

    @OneToMany(mappedBy = "testRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestStep> testSteps;

    @NotNull
    private Date runTimestamp;

    public TestRun() {
        this.uuid = UUID.randomUUID().toString();
    }

    public boolean calculateAggregatedStatusForTestSteps() {
        return testSteps.stream()
                .map(TestStep::getComparisonResult)
                .allMatch(ComparisonResult::isPassed);
    }

    public String getUsername() {
        return testCase.getUsername();
    }

    public Set<String> getScreenshotPaths() {
        Set<String> paths = new HashSet<>();
        testSteps.forEach(step -> {
            ComparisonResult result = step.getComparisonResult();

            if (result.getCurrentScreenshotPath() != null) {
                paths.add(result.getCurrentScreenshotPath());
            }

            if (result.getDiffScreenshotPath() != null) {
                paths.add(result.getDiffScreenshotPath());
            }

            ComparisonSettings settings = step.getComparisonSettings();
            if (settings.getBaselineScreenshotPath() != null) {
                paths.add(settings.getBaselineScreenshotPath());
            }

        });

        return paths;
    }
}
