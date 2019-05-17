package com.codete.regression.api.testgenerator.engine;

import com.codete.regression.api.testgenerator.TestCaseGeneratorRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.regex.Matcher;

@Slf4j
public class SeleniumTestGenerator {

    private final String testFileDirectory;
    private final String screenshotsDirectoryPath;
    private final TestContentGenerator testContentGenerator;
    private final ScreenshotSaver screenshotSaver = new ScreenshotSaver();
    private final TestSaver testSaver = new TestSaver();

    public SeleniumTestGenerator(String directoryPostfix, TestCaseGeneratorRequest testCaseGeneratorRequest) {
        String directoryPostfixWithUrl = directoryPostfix + File.separator
                + escapeInvalidUrlCharacters(testCaseGeneratorRequest.getPageUrl());
        this.screenshotsDirectoryPath = testCaseGeneratorRequest.getScreenshotsDirectoryPath()
                + File.separator + directoryPostfixWithUrl;
        this.testFileDirectory = getTestFileDirectory(testCaseGeneratorRequest, directoryPostfixWithUrl);
        this.testContentGenerator = new TestContentGenerator(testCaseGeneratorRequest, directoryPostfixWithUrl);
    }

    public void generateTest(String actionToPerform, DynamicElementDetectionResult dynamicElementDetectionResult,
                             int testCounter) {
        GeneratedTest generatedTest = testContentGenerator.generateTest(actionToPerform, testCounter);
        screenshotSaver.saveScreenshots(dynamicElementDetectionResult, screenshotsDirectoryPath, testCounter);
        testSaver.saveTest(testFileDirectory, generatedTest);
    }

    private String getTestFileDirectory(TestCaseGeneratorRequest testCaseGeneratorRequest, String directoryPostfix) {
        String packageDirectory = testCaseGeneratorRequest.getTestPackage().replaceAll("\\.",
                Matcher.quoteReplacement(File.separator));
        String directoryPath = testCaseGeneratorRequest.getGeneratedTestDirectoryPath()
                + File.separator + packageDirectory + File.separator + directoryPostfix + File.separator;
        (new File(directoryPath)).mkdirs();
        return directoryPath;
    }

    private String escapeInvalidUrlCharacters(String url) {
        return url.replaceAll("[^a-zA-Z0-9]", "_");
    }
}
