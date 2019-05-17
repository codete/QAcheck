package com.codete.regression.crawler;

import com.codete.regression.crawler.browserstack.Browser;
import com.codete.regression.crawler.browserstack.BrowserResolutionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrawlerRequest {

    private String sitemapPath;
    private String appName;
    private BrowserResolutionDto browserResolution;
    private Browser browser;
    private boolean detectDynamicElements;
}
