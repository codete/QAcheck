package com.codete.regression.testengine.testgroup;

import com.codete.regression.testengine.testgroup.request.AddTestCaseRequest;
import com.codete.regression.testengine.testgroup.request.TestGroupCreateRequest;
import com.codete.regression.testengine.userapp.UserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("test-group")
@RequiredArgsConstructor
public class TestGroupController {
    private final UserAppService userAppService;
    private final TestGroupService testGroupService;

    @PreAuthorize("@userAppService.doesUserHaveAccess( #request.appId, authentication.name )")
    @PutMapping
    public void createTestGroup(@Valid @RequestBody TestGroupCreateRequest request) {
        testGroupService.createTestGroup(request);
    }

    @PutMapping("{groupUuid}/cases")
    public void addTestCaseToTestGroup(@PathVariable("groupUuid") String groupUuid,
                                       @Valid @RequestBody AddTestCaseRequest request) {
        testGroupService.addTestCaseToTestGroup(groupUuid, request);
    }
}
