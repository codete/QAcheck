package com.codete.regression.api.testgenerator.engine;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
class TestSaver {

    private static final String JAVA_TEST_EXTENSION = ".java";

    void saveTest(String directoryPath, GeneratedTest generatedTest) {
        File testFile = new File(directoryPath + generatedTest.getClassName() + JAVA_TEST_EXTENSION);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write(generatedTest.getContent());
        } catch (
                IOException e) {
            log.info("Couldn't create test file.", e);
        }
    }

}
