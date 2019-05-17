package com.codete.regression.crawler;

class CrawlerTask implements Runnable {

    private final String username;
    private final CrawlerRequest crawlerRequest;
    private final CrawlerService crawlerService;

    CrawlerTask(String username, CrawlerRequest crawlerRequest, CrawlerService crawlerService) {
        this.username = username;
        this.crawlerRequest = crawlerRequest;
        this.crawlerService = crawlerService;
    }

    @Override
    public void run() {
        crawlerService.runCrawler(username, crawlerRequest);
    }
}
