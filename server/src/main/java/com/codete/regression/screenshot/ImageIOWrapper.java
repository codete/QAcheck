package com.codete.regression.screenshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@Slf4j
public class ImageIOWrapper {

    private static final String SCREENSHOT_EXTENSION = "png";

    public BufferedImage convertToBufferedImage(byte[] bytesArray) {
        try {
            InputStream in = new ByteArrayInputStream(bytesArray);
            return ImageIO.read(in);
        } catch (IOException e) {
            log.error("Error during reading screenshot", e);
            throw new RuntimeException("Unable to read image.");
        }
    }

    public byte[] convertToBytesArray(BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, SCREENSHOT_EXTENSION, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error during converting screenshot to bytes array", e);
            throw new RuntimeException("Unable to read image.");
        }
    }

    String convertScreenshotToBase64(BufferedImage bufferedImage) {
        byte[] bytes = convertToBytesArray(bufferedImage);
        return Base64.getEncoder().encodeToString(bytes);
    }

    BufferedImage readImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            log.error("Error during reading screenshot", e);
            throw new RuntimeException("Unable to read image with path=" + file.getPath() + ".");
        }
    }

    File saveImage(BufferedImage image, String screenshotStorageLocation, String fileName) {
        String fileNameWithExtension = fileName + "." + SCREENSHOT_EXTENSION;
        File imageFile = new File(screenshotStorageLocation + File.separator + fileNameWithExtension);
        try {
            ImageIO.write(image, SCREENSHOT_EXTENSION, imageFile);
            return imageFile;
        } catch (IOException e) {
            log.error("Error during saving screenshot", e);
            throw new RuntimeException("Unable to save image with path=" + imageFile.getPath() + ".");
        }
    }


}
