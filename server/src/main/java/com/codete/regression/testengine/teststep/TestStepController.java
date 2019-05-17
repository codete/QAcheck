package com.codete.regression.testengine.teststep;

import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsDto;
import com.codete.regression.testengine.teststepdetails.TestStepDetailsDto;
import com.codete.regression.testengine.userdecision.UserDecision.UserDecisionEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("test-steps")
public class TestStepController {

    private final TestStepService testStepService;

    public TestStepController(TestStepService testStepService) {
        this.testStepService = testStepService;
    }

    @PreAuthorize("@testRunService.doesUserHaveAccess( #testRunUuid, authentication.name )")
    @GetMapping
    public List<TestStepDto> getTestSteps(@RequestParam("testRunUuid") String testRunUuid) {
        return testStepService.getStepsForTestRunUuid(testRunUuid)
                .stream()
                .map(TestStepDto::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("@testStepService.doesUserHaveAccess( #testStepId, authentication.name )")
    @GetMapping("/{testStepId}/details")
    public TestStepDetailsDto getTestStepDetails(@PathVariable long testStepId) {
        return testStepService.getTestStepDetails(testStepId);
    }

    @PreAuthorize("@testStepService.doesUserHaveAccess( #testStepId, authentication.name )")
    @PutMapping("/{testStepId}/user-decision")
    public boolean saveUserDecision(@PathVariable("testStepId") long testStepId,
                                    @RequestBody int userDecision) {
        return testStepService.saveUserDecision(testStepId, UserDecisionEnum.getUserDecisionForNumber(userDecision));
    }

    @PreAuthorize("@testStepService.doesUserHaveAccess( #testStepId, authentication.name )")
    @PutMapping("/{testStepId}/comparison-settings")
    public void saveComparisonSettings(@PathVariable("testStepId") long testStepId,
                                       @Valid @RequestBody ComparisonSettingsDto comparisonSettings) {
        testStepService.saveComparisonSettings(testStepId, comparisonSettings);
    }

    @PreAuthorize("@testStepService.doesUserHaveAccess( #testStepId, authentication.name )")
    @PutMapping("/{testStepId}/repeat-comparison")
    public boolean repeatComparison(@PathVariable("testStepId") long testStepId) {
        return testStepService.repeatComparison(testStepId);
    }
}
