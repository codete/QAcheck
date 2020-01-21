package com.codete.regression.e2e;

import com.codete.regression.api.testgenerator.TestCaseGenerator;
import com.codete.regression.api.testgenerator.TestCaseGeneratorRequest;
import com.codete.regression.config.TestApplicationConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TestCaseGeneratorTest extends TestApplicationConfig {

    private static RemoteWebDriver driver;
    private static String pageFileUrl;
    private static String serverUrl;

    @Rule
    private TemporaryFolder temporaryFolder = new TemporaryFolder();

    @LocalServerPort
    private int serverPort;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.phantomjs().setup();
        driver = new PhantomJSDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.close();
    }

    @BeforeEach
    void setUp() throws URISyntaxException {
        File page = new File(getClass().getClassLoader().getResource("pages/screenshotComparator.html").toURI());
        pageFileUrl = "file:///" + page.getAbsolutePath();
        serverUrl = "http://localhost:" + serverPort;
    }

    @Test
    void shouldGenerateTestScenarios() throws IOException {
        temporaryFolder.create();
        File testsDirectory = temporaryFolder.newFolder();
        File screenshotsDirectory = temporaryFolder.newFolder();
        TestCaseGeneratorRequest testCaseGeneratorRequest = createTestCaseGeneratorRequest(testsDirectory, screenshotsDirectory);
        TestCaseGenerator testCaseGenerator = new TestCaseGenerator(serverUrl, API_KEY);
        testCaseGenerator.generateTestCases(testCaseGeneratorRequest, driver);

        long testCounter = fileCount(testsDirectory.toPath());
        assertThat("Test scenario should be generated", testCounter, is(2L));
        long screenshotsCounter = fileCount(screenshotsDirectory.toPath());
        assertThat("Screenshots for test scenario should be created", screenshotsCounter, is(6L));
    }

    private TestCaseGeneratorRequest createTestCaseGeneratorRequest(File testsDirectory, File screenshotsDirectory) {
        TestCaseGeneratorRequest testCaseGeneratorRequest = new TestCaseGeneratorRequest();
        testCaseGeneratorRequest.setAppName("generatedTests");
        testCaseGeneratorRequest.setGeneratedTestDirectoryPath(testsDirectory.getAbsolutePath());
        testCaseGeneratorRequest.setScreenshotsDirectoryPath(screenshotsDirectory.getAbsolutePath());
        testCaseGeneratorRequest.setPageUrl(pageFileUrl);
        testCaseGeneratorRequest.setTestPackage("com.codete.test.generated");
        testCaseGeneratorRequest.setClassNamePrefix("Codete");
        return testCaseGeneratorRequest;
    }

    private long fileCount(Path dir) throws IOException {
        return Files.walk(dir)
                .parallel()
                .filter(p -> !p.toFile().isDirectory())
                .count();
    }

}