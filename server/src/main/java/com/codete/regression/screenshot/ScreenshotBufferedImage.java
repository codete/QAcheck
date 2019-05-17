package com.codete.regression.screenshot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
@AllArgsConstructor
public class ScreenshotBufferedImage {

    private String relativePath;
    private BufferedImage screenshot;
}
