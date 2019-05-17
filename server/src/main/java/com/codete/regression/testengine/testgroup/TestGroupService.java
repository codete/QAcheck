package com.codete.regression.testengine.testgroup;

import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.testcase.TestCaseService;
import com.codete.regression.testengine.testgroup.exception.NotFoundException;
import com.codete.regression.testengine.testgroup.request.AddTestCaseRequest;
import com.codete.regression.testengine.testgroup.request.TestGroupCreateRequest;
import com.codete.regression.testengine.userapp.UserApp;
import com.codete.regression.testengine.userapp.UserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestGroupService {
    private final TestGroupRepository testGroupRepository;
    private final UserAppService userAppService;
    private final TestCaseService testCaseService;

    @Transactional
    public void createTestGroup(TestGroupCreateRequest request) {
        TestGroup group = new TestGroup();
        UserApp userApp = userAppService.findById(request.getAppId())
                .orElseThrow(() -> new NotFoundException("Unable to find user app"));

        if (request.getParentUuid() != null) {
            TestGroup parent = testGroupRepository.findByUuid(request.getParentUuid())
                    .orElseThrow(() -> new NotFoundException("Unable to find parent group"));
            group.setParent(parent);
        }

        group.setUserApp(userApp);
        group.setCaption(request.getCaption());
        testGroupRepository.save(group);
    }

    @Transactional
    public void addTestCaseToTestGroup(String groupUuid, AddTestCaseRequest request) {
        TestGroup group = testGroupRepository.findByUuid(groupUuid)
                .orElseThrow(() -> new NotFoundException("Unable to find group"));

        TestCase testCase = testCaseService.findByUuid(request.getTestCaseUuid())
                .orElseThrow(() -> new NotFoundException("Unable to find test case"));

        group.getCases().add(testCase);
        testGroupRepository.save(group);
    }
}
