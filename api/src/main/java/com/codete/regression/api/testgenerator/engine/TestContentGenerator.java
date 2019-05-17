package com.codete.regression.api.testgenerator.engine;

import com.codete.regression.api.testgenerator.TestCaseGeneratorRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
class TestContentGenerator {

    private static final String TEMPLATE_FILE = "/generated_test_templates/SeleniumTest.txt";
    private static final String APP_NAME_KEY = "\\{appName}";
    private static final String PACKAGE_KEY = "\\{package}";
    private static final String CLASS_NAME_KEY = "\\{className}";
    private static final String PAGE_URL_KEY = "\\{pageUrl}";
    private static final String TEST_NAME_KEY = "\\{testName}";
    private static final String ACTION = "\\{actionToPerform}";
    private static final String API_KEY = "\\{apiKey}";
    private static final String BEFORE_EACH = "\\{beforeEach}";
    private static final String CUSTOM_CODE = "\\{customCode}";
    private final TestCaseGeneratorRequest testCaseGeneratorRequest;
    private final String testNamePrefix;
    private final String templateContent;
    private final String classPackage;

    TestContentGenerator(TestCaseGeneratorRequest testCaseGeneratorRequest,
                         String directoryPostfixWithUrl) {
        this.testCaseGeneratorRequest = testCaseGeneratorRequest;
        this.testNamePrefix = directoryPostfixWithUrl.replace(File.separator, "_") + "_";
        this.classPackage = testCaseGeneratorRequest.getTestPackage() + "."
                + directoryPostfixWithUrl.replace(File.separator, ".");
        this.templateContent = readTemplateFile();
    }

    GeneratedTest generateTest(String action, int testIndex) {
        String className = testCaseGeneratorRequest.getClassNamePrefix() + testIndex;
        String content = templateContent.replaceAll(APP_NAME_KEY, testCaseGeneratorRequest.getAppName())
                .replaceAll(PACKAGE_KEY, classPackage)
                .replaceAll(CLASS_NAME_KEY, className)
                .replaceAll(PAGE_URL_KEY, testCaseGeneratorRequest.getPageUrl())
                .replaceAll(TEST_NAME_KEY, testNamePrefix + testIndex)
                .replaceAll(ACTION, action)
                .replaceAll(BEFORE_EACH, testCaseGeneratorRequest.getBeforeEach() != null
                        ? testCaseGeneratorRequest.getBeforeEach() : "")
                .replaceAll(CUSTOM_CODE, testCaseGeneratorRequest.getCustomCode() != null
                        ? testCaseGeneratorRequest.getCustomCode() : "")
                .replaceAll(API_KEY, testCaseGeneratorRequest.getApiKey());
        return new GeneratedTest(content, className);
    }

    private String readTemplateFile() {
        InputStream inputStream = TestContentGenerator.class.getResourceAsStream(TEMPLATE_FILE);
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("Cannot generate tests.");
        }
    }
}
