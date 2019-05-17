package com.codete.regression.crawler.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CrawlerInstanceDto {

    private String currentLog;

    private float progress;

    private boolean active;

    public CrawlerInstanceDto(CrawlerInstance crawlerInstance) {
        this.progress = crawlerInstance.getProgress();
        this.currentLog = crawlerInstance.getCurrentLog();
        this.active = crawlerInstance.getProgress() < 100;
    }
}
