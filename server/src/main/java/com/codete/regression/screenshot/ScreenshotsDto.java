package com.codete.regression.screenshot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreenshotsDto {

    private String baselineScreenshot;
    private String currentScreenshot;
    private String diffScreenshot;

}
