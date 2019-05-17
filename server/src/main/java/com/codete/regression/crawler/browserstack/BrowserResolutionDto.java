package com.codete.regression.crawler.browserstack;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BrowserResolutionDto {
    private int width;
    private int height;

    public BrowserResolutionDto(BrowserResolution browserResolution) {
        this.height = browserResolution.getHeight();
        this.width = browserResolution.getWidth();
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
