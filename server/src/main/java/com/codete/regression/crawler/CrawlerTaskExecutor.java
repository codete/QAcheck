package com.codete.regression.crawler;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
class CrawlerTaskExecutor {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final CrawlerService crawlerService;

    public CrawlerTaskExecutor(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    void runCrawlerTask(String username, CrawlerRequest crawlerRequest) {
        CrawlerTask crawlerTask = new CrawlerTask(username, crawlerRequest, crawlerService);
        executorService.submit(crawlerTask);
    }
}
