package com.codete.regression.testengine.testcase;

import com.codete.regression.api.testengine.TestRunRequest;
import com.codete.regression.authentication.user.User;
import com.codete.regression.screenshot.ScreenshotStorage;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettingsRepository;
import com.codete.regression.testengine.environment.Environment;
import com.codete.regression.testengine.environment.EnvironmentService;
import com.codete.regression.testengine.testcase.request.MultiTestCaseRequest;
import com.codete.regression.testengine.userapp.UserApp;
import com.codete.regression.testengine.userapp.UserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final ComparisonSettingsRepository comparisonSettingsRepository;
    private final EnvironmentService environmentService;
    private final UserAppService userAppService;
    private final ScreenshotStorage screenshotStorage;

    @Transactional
    public TestCase save(TestCase testCase) {
        return testCaseRepository.save(testCase);
    }

    @Transactional
    public TestCase findTestCaseOrCreateNewOne(User user, TestRunRequest request) {
        Environment environment = environmentService.findByEnvironmentSettingsOrCreateNewOne(
                request.getEnvironmentSettings());
        UserApp userApp = userAppService.findUserAppOrCreateNewOne(user, request.getAppName());
        return testCaseRepository.findByUserAppAndTestNameAndEnvironment_Id(userApp, request.getTestName(),
                environment.getId())
                .orElseGet(() -> save(new TestCase(userApp, request.getTestName(), environment)));
    }

    @Transactional(readOnly = true)
    public Page<TestCase> getAllTestCases(MultiTestCaseRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getLimit());
        Specification<TestCase> specification = TestCaseSpecifications
                .byUsernameAndAppName(request.getUsername(), request.getAppName())
                .and(TestCaseSpecifications.byFilters(request.getFilters()));
        return testCaseRepository.findAll(specification, pageRequest);
    }

    @Transactional(readOnly = true)
    public Optional<TestCase> findByUuid(String uuid) {
        return testCaseRepository.findByUuid(uuid);
    }

    @Transactional
    public void deleteTestCase(String testCaseUuid) {
        testCaseRepository.findByUuid(testCaseUuid)
                .ifPresent(testCase -> {
                    String screenshotsFolder = screenshotStorage.getTestCaseStorageLocation(testCase);
                    testCaseRepository.delete(testCase);
                    comparisonSettingsRepository.findAllOrphans()
                            .map(ComparisonSettings::getId)
                            .forEach(comparisonSettingsRepository::deleteById);

                    screenshotStorage.deleteScreenshots(screenshotsFolder);
                });
    }

    @Transactional
    public boolean doesUserHaveAccess(String testCaseUuid, String username) {
        return testCaseRepository.findByUuid(testCaseUuid)
                .map(testCase -> testCase.getUsername().equals(username))
                .orElse(false);
    }
}
