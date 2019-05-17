package com.codete.regression.api.crawler;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

@Slf4j
public class SitemapCrawler {

    private final SitemapUrlsRetriever sitemapUrlsRetriever = new SitemapUrlsRetriever();
    private final SeleniumCrawler seleniumCrawler;

    public SitemapCrawler(String serverUrl, String apiKey, String appName, boolean detectAndIgnoreDynamicAreas) {
        this.seleniumCrawler = new SeleniumCrawler(serverUrl, apiKey, appName, detectAndIgnoreDynamicAreas);
    }

    public boolean crawlThroughSitemap(WebDriver webDriver, URL sitemapUrl) {
        log.info("Started downloading sitemap from URL={}", sitemapUrl);
        boolean result = false;
        try {
            InputStream sitemap = sitemapUrl.openStream();
            result = runSeleniumCrawler(webDriver, sitemap);
        } catch (IOException e) {
            log.error("Error during retrieving sitemap from url={}", sitemapUrl, e);
        }
        return result;
    }

    public boolean crawlThroughSitemap(WebDriver webDriver, File sitemapFile) {
        log.info("Started crawling through sitemap file={}", sitemapFile.getName());
        boolean result = false;
        try {
            InputStream sitemap = new FileInputStream(sitemapFile);
            result = runSeleniumCrawler(webDriver, sitemap);
        } catch (FileNotFoundException e) {
            log.error("Sitemap file={} not found", sitemapFile.getPath(), e);
        }
        return result;
    }

    private boolean runSeleniumCrawler(WebDriver webDriver, InputStream sitemap) {
        Set<String> sitemapUrls = sitemapUrlsRetriever.getUrls(sitemap);
        boolean result = seleniumCrawler.takeScreenshotsAndCompareWithTheBaseline(webDriver,
                sitemapUrls);
        log.info("Crawling finished.");
        return result;
    }


}
