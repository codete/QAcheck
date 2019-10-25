package com.codete.regression.screenshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Profile("!test")
@Service
@Slf4j
public class ScreenshotStorageFileSystem implements ScreenshotStorage {

    private final String screenshotsDirectoryRootPath;
    private final ImageIOWrapper imageIOWrapper;

    ScreenshotStorageFileSystem(Environment environment, ImageIOWrapper imageIOWrapper) {
        String screenshotsDirectoryPathProperty = environment.getProperty("screenshots.directory.path");
        if (screenshotsDirectoryPathProperty == null || screenshotsDirectoryPathProperty.isBlank()) {
            throw new RuntimeException("screenshots.directory.path is not defined. " +
                    "Set this property in your application.yaml file.");
        }
        this.screenshotsDirectoryRootPath = screenshotsDirectoryPathProperty + File.separator;
        this.imageIOWrapper = imageIOWrapper;
    }

    @Override
    public ScreenshotBufferedImage readScreenshot(String screenshotPath) {
        File file = new File(screenshotsDirectoryRootPath + screenshotPath);
        BufferedImage bufferedImage = imageIOWrapper.readImage(file);
        return new ScreenshotBufferedImage(screenshotPath, bufferedImage);
    }

    @Override
    public ScreenshotBufferedImage saveScreenshot(BufferedImage image, String screenshotStorageLocation, String fileName) {
        String fullPath = screenshotsDirectoryRootPath + screenshotStorageLocation;
        createDirectoryIfItDoesNotExists(fullPath);
        File file = imageIOWrapper.saveImage(image, fullPath, fileName);
        return new ScreenshotBufferedImage(screenshotStorageLocation + File.separator + file.getName(), image);
    }

    @Override
    public void deleteScreenshots(String screenshotsPath) {
        try {
            Files.walk(Path.of(screenshotsDirectoryRootPath + screenshotsPath))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            log.warn("Screenshots couldn't be deleted.");
        }
    }

    @Override
    public void deleteByFileName(String screenshotStorageLocation, String fileName) {
        try {
            Path dirPath = Path.of(screenshotsDirectoryRootPath + screenshotStorageLocation);
            if (!dirPath.toFile().exists()) {
                return;
            }
            Files.walk(dirPath)
                    .map(Path::toFile)
                    .filter(file -> file.getName().startsWith(fileName))
                    .filter(File::exists)
                    .forEach(File::delete);
        } catch (IOException ex) {
            log.warn("Screenshot `" + fileName + "` couldn't be deleted.", ex);
            throw new RuntimeException("Unable to save image with path=" + screenshotStorageLocation + File.separator + fileName);
        }
    }

    private void createDirectoryIfItDoesNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean result = directory.mkdirs();
            if (!result) {
                throw new RuntimeException("Unable to create directories");
            }
        }
    }

}
