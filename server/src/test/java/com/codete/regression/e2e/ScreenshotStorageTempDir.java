package com.codete.regression.e2e;

import com.codete.regression.screenshot.ScreenshotBufferedImage;
import com.codete.regression.screenshot.ScreenshotStorage;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Profile("test")
@Service
public class ScreenshotStorageTempDir implements ScreenshotStorage {

    private static final String SCREENSHOT_FORMAT = "png";
    private final File tempDir;
    @Rule
    private TemporaryFolder temporaryFolder = new TemporaryFolder();

    public ScreenshotStorageTempDir() throws IOException {
        temporaryFolder.create();
        tempDir = temporaryFolder.getRoot();
    }

    @Override
    public ScreenshotBufferedImage readScreenshot(String screenshotPath) {
        try {
            File file = new File(screenshotPath);
            BufferedImage bufferedImage = ImageIO.read(file);
            return new ScreenshotBufferedImage(screenshotPath, bufferedImage);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read image with path=" + screenshotPath + ".", e);
        }
    }

    @Override
    public ScreenshotBufferedImage saveScreenshot(BufferedImage image, String directoryRelativePath, String fileName) {
        try {
            File directory = new File(tempDir.getPath() + File.separator + directoryRelativePath);
            directory.mkdirs();
            File imageFile = new File(directory.getPath() + File.separator + fileName + "." + SCREENSHOT_FORMAT);
            ImageIO.write(image, SCREENSHOT_FORMAT, imageFile);
            return new ScreenshotBufferedImage(imageFile.getPath(), image);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save image with name=" + fileName + ".", e);
        }
    }

    @Override
    public void deleteScreenshots(String screenshotsPath) {
        try {
            Files.walk(Path.of(screenshotsPath))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException("Screenshots couldn't be deleted.", e);
        }
    }

}
