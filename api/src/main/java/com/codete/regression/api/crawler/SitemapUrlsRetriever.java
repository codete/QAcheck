package com.codete.regression.api.crawler;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SitemapUrlsRetriever {

    private static final String SITEMAP_FILE = "sitemap.xml";
    private final SitemapParser sitemapParser = new SitemapParser();

    public Set<String> getUrls(InputStream sitemap) {
        Set<String> sitemapUrls = sitemapParser.getAllUrls(sitemap);
        return retrieveAllUrlsRecursively(sitemapUrls, new HashSet<>());
    }

    private Set<String> retrieveAllUrlsRecursively(Set<String> urls, Set<String> analyzedSitemaps) {
        Set<String> result = new HashSet<>();
        for (String url : urls) {
            if (analyzedSitemaps.contains(url)) {
                continue;
            }
            if (url.toLowerCase().endsWith(SITEMAP_FILE)) {
                analyzedSitemaps.add(url);
                Set<String> sitemapUrls = getUrlsFromSitemap(url);
                Set<String> recursiveUrls = retrieveAllUrlsRecursively(sitemapUrls, analyzedSitemaps);
                result.addAll(recursiveUrls);
            } else {
                result.add(url);
            }
        }
        return result;
    }

    private Set<String> getUrlsFromSitemap(String sitemapUrl) {
        Set<String> urls = new HashSet<>();
        try {
            URL url = new URL(sitemapUrl);
            InputStream sitemap = url.openStream();
            urls = sitemapParser.getAllUrls(sitemap);
        } catch (MalformedURLException e) {
            log.warn("Cannot parse URL={}", sitemapUrl, e.getMessage());
        } catch (IOException e) {
            log.error("Error during retrieving sitemap from url={}", sitemapUrl, e);
        }
        return urls;
    }

}
