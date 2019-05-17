package com.codete.regression.api.testgenerator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TestCaseGeneratorRequest {

    @NotNull
    private String apiKey;

    /**
     * Generator will generate test for this page
     */
    @NotNull
    private String pageUrl;

    /**
     * Directory where tests will be saved.
     */
    @NotNull
    private String generatedTestDirectoryPath;

    /**
     * Directory where screenshots taken during test generation will be saved.
     */
    @NotNull
    private String screenshotsDirectoryPath;

    /**
     * Java package of generated tests.
     */
    @NotNull
    private String testPackage;

    /**
     * This name will be saved in java test file and will be visible on Regression testing platform GUI
     * when you run such test.
     */
    @NotNull
    private String appName;

    /**
     * Prefix which will be added to each generated java class.
     */
    @NotNull
    private String classNamePrefix;

    /**
     * Code which will be added in the @BeforeEach block.
     */
    private String beforeEach;

    /**
     * Code which will be added at the end of the generated test.
     */
    private String customCode;

    /**
     * Set to true if you want generator to run crawler
     * which will search for more pages and generate separate scenarios for them.
     */
    private boolean runWithCrawler;

    private int crawlerMaxPagesToVisit;

}
