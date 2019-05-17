package com.codete.regression.crawler.browserstack;

import lombok.Getter;

@Getter
enum WindowsBrowserResolutionEnum implements BrowserResolution {

    _1024x768(1024, 768),
    _1280x800(1280, 800),
    _1366x768(1366, 768),
    _1440x900(1440, 900),
    _1280x1024(1280, 1024),
    _1680x1050(1680, 1050),
    _1600x1200(1600, 1200),
    _1920x1200(1920, 1200),
    _1920x1080(1920, 1080),
    _2048x1536(2048, 1536);

    static final String OS_NAME = "windows";
    private int width;
    private int height;

    WindowsBrowserResolutionEnum(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
