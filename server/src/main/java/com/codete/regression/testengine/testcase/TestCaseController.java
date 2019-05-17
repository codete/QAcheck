package com.codete.regression.testengine.testcase;

import com.codete.regression.http.CustomHeaders;
import com.codete.regression.testengine.testcase.request.MultiTestCaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("test-cases")
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @PreAuthorize("#request.username == authentication.name")
    @GetMapping
    public ResponseEntity<List<TestCaseDto>> getAllTestCases(@Valid MultiTestCaseRequest request) {
        Page<TestCase> testCasesPage = testCaseService.getAllTestCases(request);

        List<TestCaseDto> cases = testCasesPage.stream()
                .map(TestCaseDto::new)
                .collect(Collectors.toList());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(CustomHeaders.TOTAL_PAGES, Integer.toString(testCasesPage.getTotalPages()));
        responseHeaders.set(CustomHeaders.TOTAL_COUNT, Long.toString(testCasesPage.getTotalElements()));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(cases);
    }

    @PreAuthorize("@testCaseService.doesUserHaveAccess( #testCaseUuid, authentication.name )")
    @GetMapping("/{testCaseUuid}")
    public TestCaseDto getTestCaseByUuid(@PathVariable("testCaseUuid") String testCaseUuid) {
        return testCaseService.findByUuid(testCaseUuid)
                .map(TestCaseDto::new)
                .orElse(null);
    }

    @PreAuthorize("@testCaseService.doesUserHaveAccess( #testCaseUuid, authentication.name )")
    @DeleteMapping("/{testCaseUuid}")
    public void deleteTestCase(@PathVariable String testCaseUuid) {
        testCaseService.deleteTestCase(testCaseUuid);
    }
}
