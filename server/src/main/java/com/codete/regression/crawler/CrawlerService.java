package com.codete.regression.crawler;

import com.codete.regression.api.crawler.SeleniumCrawler;
import com.codete.regression.api.crawler.SitemapUrlsRetriever;
import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.UserService;
import com.codete.regression.crawler.browserstack.BrowserStackService;
import com.codete.regression.crawler.instance.CrawlerInstance;
import com.codete.regression.crawler.instance.CrawlerInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Slf4j
@Service
public class CrawlerService {

    private final CrawlerInstanceService crawlerInstanceService;
    private final UserService userService;
    private final BrowserStackService browserStackService;
    private final SitemapUrlsRetriever sitemapUrlsRetriever = new SitemapUrlsRetriever();

    @Value("${crawler.server.url}")
    private String serverUrl;

    public CrawlerService(CrawlerInstanceService crawlerInstanceService, UserService userService,
                          BrowserStackService browserStackService) {
        this.crawlerInstanceService = crawlerInstanceService;
        this.userService = userService;
        this.browserStackService = browserStackService;
    }

    void runCrawler(String username, CrawlerRequest crawlerRequest) {
        CrawlerInstance crawlerInstance = crawlerInstanceService.startNewInstanceForUser(username, "Downloading sitemap.");
        try {
            User user = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User doesn't exist."));
            URL sitemapUrl = new URL(crawlerRequest.getSitemapPath());
            Set<String> urls = sitemapUrlsRetriever.getUrls(sitemapUrl.openStream());
            crawlThroughSitemap(crawlerInstance, user.getApiKey(), crawlerRequest, urls);
        } catch (MalformedURLException e) {
            log.error("Error during parsing URL={}", crawlerRequest.getSitemapPath(), e);
            throw new IllegalArgumentException(String.format("Error during parsing URL=%s", crawlerRequest.getSitemapPath()), e);
        } catch (IOException ioe) {
            log.error("Error during retrieving sitemap from url={}", crawlerRequest.getSitemapPath(), ioe);
            throw new IllegalArgumentException(String.format("Error during retrieving sitemap from url=%s",
                    crawlerRequest.getSitemapPath()), ioe);
        } finally {
            crawlerInstanceService.delete(crawlerInstance);
        }
    }

    private void crawlThroughSitemap(CrawlerInstance crawlerInstance, String apiKey, CrawlerRequest crawlerRequest,
                                     Set<String> urls) {
        if (!crawlerInstanceService.updateIfNotStopped(crawlerInstance.getId(), "Setuping BrowserStack driver", 0.0f)) {
            return;
        }

        WebDriver driver = browserStackService.createRemoteWebDriver(crawlerRequest);
        try {
            SeleniumCrawler seleniumCrawler = new SeleniumCrawler(serverUrl, apiKey, crawlerRequest.getAppName(),
                    crawlerRequest.isDetectDynamicElements());
            float currentPage = 0;
            for (String url : urls) {
                seleniumCrawler.takeScreenshotAndCompareWithTheBaseline(driver, url);
                currentPage++;
                float progress = currentPage / urls.size() * 100;
                String currentLog = String.format("Testing page, url=%s", url);
                if (!crawlerInstanceService.updateIfNotStopped(crawlerInstance.getId(), currentLog, progress)) {
                    return;
                }

            }
        } catch (Exception e) {
            log.error("Error during crawl through sitemap", e);
            throw e;
        } finally {
            if (driver != null) {
                log.info("Quiting selenium driver.");
                driver.quit();
            }
        }
    }
}
