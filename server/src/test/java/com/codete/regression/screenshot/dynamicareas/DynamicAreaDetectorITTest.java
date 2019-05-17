package com.codete.regression.screenshot.dynamicareas;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.config.TestApplicationConfig;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Slf4j
class DynamicAreaDetectorITTest extends TestApplicationConfig {

    private static final int IMAGE_WIDTH = 617;
    private static final int IMAGE_HEIGHT = 420;
    private static final String[] SCREENSHOT_FILE_NAMES = new String[]{"1.png", "2.png", "3.png"};
    private static final String DYNAMIC_AREAS_IMAGES_DIR = "images/dynamicareas/";
    private static final String DYNAMIC_AREAS_RESULT_IMAGE = "dynamicAreas.png";

    @Autowired
    private DynamicAreaDetector dynamicAreaDetector;

    @Test
    void shouldInitIgnoreAreasForDynamicElements() throws IOException, URISyntaxException {
        List<Screenshot> screenshots = new ArrayList<>();
        for (String fileName : SCREENSHOT_FILE_NAMES) {
            screenshots.add(readScreenshot(fileName));
        }

        List<IgnoreArea> ignoreAreas = dynamicAreaDetector.createIgnoreAreasForDynamicElements(new ComparisonSettings(),
                screenshots);

        assertThat(ignoreAreas, hasSize(5));
        BufferedImage imageWithIgnoreAreas = IgnoreAreaPainter.createImageWithIgnoreAreas(IMAGE_WIDTH, IMAGE_HEIGHT,
                ignoreAreas);
        boolean imagesComparisonResult = compareImages(imageWithIgnoreAreas,
                ImageIO.read(getScreenshotFile(DYNAMIC_AREAS_RESULT_IMAGE)));
        if (!imagesComparisonResult) {
            saveImage(imageWithIgnoreAreas);
        }
        assertThat(imagesComparisonResult, is(true));
    }

    private Screenshot readScreenshot(String fileName) throws IOException, URISyntaxException {
        File file = getScreenshotFile(fileName);
        return new Screenshot("", Files.readAllBytes(file.toPath()));
    }

    private File getScreenshotFile(String fileName) throws URISyntaxException {
        return new File(getClass().getClassLoader().getResource(DYNAMIC_AREAS_IMAGES_DIR + fileName).toURI());
    }

    private void saveImage(BufferedImage image) {
        File targetClassesDir = new File(DynamicAreaDetectorITTest.class.getProtectionDomain().getCodeSource()
                .getLocation().getPath());
        File imageFile = new File(targetClassesDir.getParentFile() + "/screenshots/"
                + DYNAMIC_AREAS_RESULT_IMAGE);
        if (targetClassesDir.exists()) {
            imageFile.mkdirs();
            try {
                ImageIO.write(image, "png", imageFile);
                log.info("Image with dynamic areas calculated during test: {}.", imageFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException("Cannot save dynamic areas screenshot.");
            }
        }
    }

    private boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width = imgA.getWidth();
        int height = imgA.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

}