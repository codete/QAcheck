package com.codete.regression.api.crawler;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Slf4j
class SitemapParser {

    Set<String> getAllUrls(InputStream sitemap) {
        Document sitemapDocument = parseSitemap(sitemap);
        NodeList locNodeList = sitemapDocument.getElementsByTagName("loc");
        Set<String> urls = new HashSet<>();
        for (int i = 0; i < locNodeList.getLength(); i++) {
            urls.add(locNodeList.item(i).getTextContent());
        }
        return urls;
    }

    private Document parseSitemap(InputStream sitemap) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(sitemap);
        } catch (Exception e) {
            log.error("Cannot retrieve sitemap", e);
            throw new RuntimeException("Cannot retrieve sitemap");
        }
    }
}
