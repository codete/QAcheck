package com.codete.regression.crawler.browserstack;

import lombok.Getter;

@Getter
enum MacBrowserResolutionEnum implements BrowserResolution {

    _1024x768(1024, 768),
    _1280x900(1280, 960),
    _1280x1024(1280, 1024),
    _1600x1200(1600, 1200),
    _1920x1080(1920, 1080);

    static final String OS_NAME = "os x";
    private int width;
    private int height;

    MacBrowserResolutionEnum(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
