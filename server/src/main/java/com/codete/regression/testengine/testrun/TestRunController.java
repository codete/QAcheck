package com.codete.regression.testengine.testrun;

import com.codete.regression.api.testengine.TestRunRequest;
import com.codete.regression.http.CustomHeaders;
import com.codete.regression.testengine.testrun.request.MultiTestRunRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("test-runs")
@Slf4j
class TestRunController {

    private final TestRunService testRunService;

    public TestRunController(TestRunService testRunService) {
        this.testRunService = testRunService;
    }

    @PreAuthorize("@testRunService.doesUserHaveAccess( #testRunUuid, authentication.name )")
    @DeleteMapping("/{testRunUuid}")
    public void deleteTestRun(@PathVariable String testRunUuid) {
        testRunService.deleteTestRun(testRunUuid);
    }

    @PreAuthorize("@testCaseService.doesUserHaveAccess( #request.testCaseUuid, authentication.name )")
    @GetMapping()
    public ResponseEntity<List<TestRunDto>> getAllTestRunsByTestCaseUuid(@Valid MultiTestRunRequest request) {
        Page<TestRun> testRunPage = testRunService.findAllTestRunsByTestCaseUuid(request);

        List<TestRunDto> runs = testRunPage.stream()
                .map(TestRunDto::new)
                .collect(Collectors.toList());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(CustomHeaders.TOTAL_PAGES, Integer.toString(testRunPage.getTotalPages()));
        responseHeaders.set(CustomHeaders.TOTAL_COUNT, Long.toString(testRunPage.getTotalElements()));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(runs);

    }

    @PostMapping(value = "/run-screenshots-comparison", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean compareScreenshotWithTheBaseline(@RequestHeader("API-key") String apiKey,
                                                    @Valid @RequestBody TestRunRequest request) {
        log.info("Received screenshot comparison request for appName={} and testName={}.",
                request.getAppName(), request.getTestName());
        TestRun testRun = testRunService.runTest(apiKey, request);
        return testRun.isPassed();
    }
}
