package com.codete.regression.testengine.testrun;

import com.codete.regression.api.testengine.TestRunRequest;
import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.UserService;
import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsRepository;
import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.testcase.TestCaseService;
import com.codete.regression.testengine.testrun.request.MultiTestRunRequest;
import com.codete.regression.testengine.teststep.TestStep;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestRunService {

    private final TestRunRepository testRunRepository;
    private final ComparisonSettingsRepository comparisonSettingsRepository;
    private final TestRunner testRunner;
    private final TestCaseService testCaseService;
    private final UserService userService;
    private final ScreenshotStorage screenshotStorage;

    @Transactional
    public void updateTestRunAggregatedStatusIfNecessary(TestRun testRun) {
        boolean aggregatedTestRunStatus = testRun.calculateAggregatedStatusForTestSteps();
        if (aggregatedTestRunStatus != testRun.isPassed()) {
            testRun.setPassed(aggregatedTestRunStatus);
            testRunRepository.save(testRun);
        }
    }

    @Transactional
    public TestRun runTest(String apiKey, TestRunRequest request) {
        Optional<User> optionalUser = userService.findByApiKey(apiKey);
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("Wrong API key."));
        TestCase testCase = testCaseService.findTestCaseOrCreateNewOne(user, request);
        TestRun testRun = new TestRun();
        List<TestStep> results = testRunner.runComparisons(testCase, testRun, request);
        testRun.setRunTimestamp(new Date());
        testRun.setTestCase(testCase);
        testRun.setTestSteps(results);
        testRun.setPassed(testRun.calculateAggregatedStatusForTestSteps());
        testCase.setLatestTestRun(testRun);
        return testRunRepository.save(testRun);
    }
    
    @Transactional(readOnly = true)
    public Page<TestRun> findAllTestRunsByTestCaseUuid(MultiTestRunRequest request) {
        return testRunRepository.findAllByTestCaseUuid(
                request.getTestCaseUuid(),
                PageRequest.of(request.getPage(), request.getLimit(), Sort.by(TestRun_.RUN_TIMESTAMP).descending())
        );
    }

    @Transactional
    public void deleteTestRun(String testRunUuid) {
        testRunRepository.findByUuid(testRunUuid)
                .ifPresent(testRun -> {
                    Set<String> screenshotsPaths = testRun.getScreenshotPaths();
                    resetLatestTestRun(testRun);
                    testRunRepository.delete(testRun);
                    comparisonSettingsRepository.findAllOrphans()
                            .map(ComparisonSettings::getId)
                            .forEach(comparisonSettingsRepository::deleteById);

                    screenshotsPaths.stream()
                            .filter(path -> comparisonSettingsRepository.countByBaselineScreenshotPath(path) == 0)
                            .forEach(screenshotStorage::deleteScreenshots);
                });
    }

    @Transactional
    public boolean doesUserHaveAccess(String testRunUuid, String username) {
        return testRunRepository.findByUuid(testRunUuid)
                .map(testRun -> testRun.getUsername().equals(username))
                .orElse(false);
    }

    private void resetLatestTestRun(TestRun testRun) {
        TestCase testCase = testRun.getTestCase();
        TestRun latestTestRun = testCase.getLatestTestRun();
        if (latestTestRun != null && latestTestRun.getUuid().equals(testRun.getUuid())) {
            TestRun newLatestTestRun = testRunRepository.findTop1ByTestCaseUuidAndUuidNotOrderByRunTimestampDesc(testCase.getUuid(), testRun.getUuid())
                    .orElse(null);
            testCase.setLatestTestRun(newLatestTestRun);
        }
    }
}
