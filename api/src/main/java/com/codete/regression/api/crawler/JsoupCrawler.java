package com.codete.regression.api.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class JsoupCrawler {
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
    private static final String URL_PROTOCOL_REGEX = "^(http[s]?://www\\.|http[s]?://|www\\.)";

    public Set<String> search(String url, int maxPagesToSearch) {
        log.info("Crawler is looking for pages. Started with url: {}, max pages to search: {}.", url, maxPagesToSearch);
        String baseUrl = getBaseUrl(url);
        Set<String> properLinks = new HashSet<>();
        Set<String> pagesToVisit = new HashSet<>();
        Map<String, String> pagesVisited = new HashMap<>();
        pagesToVisit.add(url);
        while (properLinks.size() <= maxPagesToSearch && !pagesToVisit.isEmpty()) {
            String currentUrl = pagesToVisit.iterator().next();
            pagesToVisit.remove(currentUrl);
            Optional<Document> htmlDocument = getHtmlDocument(currentUrl);
            htmlDocument.ifPresent(document -> {
                properLinks.add(currentUrl);
                Map<String, String> links = getPageLinks(baseUrl, document);
                log.info("Found {} link(s).", links.size());
                pagesVisited.put(currentUrl.replaceFirst(URL_PROTOCOL_REGEX, ""), currentUrl);
                links.forEach((key, value) -> {
                    if (!pagesVisited.containsKey(key)) {
                        pagesToVisit.add(value);
                    }
                });
            });
        }
        log.info("Links: {}", properLinks);
        return properLinks;
    }

    private String getBaseUrl(String url) {
        String urlWithoutProtocol = url.replaceFirst(URL_PROTOCOL_REGEX, "");
        int slashIndex = urlWithoutProtocol.indexOf("/");
        if (slashIndex != -1) {
            return urlWithoutProtocol.substring(0, slashIndex);
        } else {
            return urlWithoutProtocol;
        }
    }

    private Optional<Document> getHtmlDocument(String url) {
        Optional<Document> result = Optional.empty();
        try {
            log.info("Visiting web page {} ", url);
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            if (connection.response().statusCode() != 200 && !connection.response().contentType().contains("text/html")) {
                log.warn("Failure. Retrieved something other than HTML or page was unreachable.");
            } else {
                result = Optional.of(htmlDocument);
            }
        } catch (IOException ioe) {
            log.warn("Error during connection to page " + url, ioe.getMessage());
        }
        return result;
    }

    private Map<String, String> getPageLinks(String baseUrl, Document htmlDocument) {
        Map<String, String> links = new HashMap<>();
        Elements linksOnPage = htmlDocument.select("a[href]");
        for (Element link : linksOnPage) {
            String href = link.absUrl("href");
            String urlWithoutProtocol = href.replaceFirst(URL_PROTOCOL_REGEX, "");
            if (urlWithoutProtocol.startsWith(baseUrl) && !href.contains("#")) {
                links.put(urlWithoutProtocol, href);
            }
        }
        return links;
    }
}