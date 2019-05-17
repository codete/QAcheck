package com.codete.regression.api.crawler;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class SitemapParserTest {

    private final SitemapParser sitemapParser = new SitemapParser();

    @Test
    void shouldReadAllUrlsFromSitemapFile() throws URISyntaxException, FileNotFoundException {
        File page = new File(getClass().getClassLoader().getResource("sitemap.xml").toURI());
        InputStream inputStream = new FileInputStream(page);

        Set<String> urls = sitemapParser.getAllUrls(inputStream);

        assertThat(urls, hasSize(3));
    }

}