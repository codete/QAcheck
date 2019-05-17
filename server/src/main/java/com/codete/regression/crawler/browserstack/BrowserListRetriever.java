package com.codete.regression.crawler.browserstack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
class BrowserListRetriever {

    private static final String BROWSERS_ENDPOINT = "https://api.browserstack.com/automate/browsers.json";
    private final String authorizationHeader;
    private final ObjectMapper objectMapper = new ObjectMapper();

    BrowserListRetriever(BrowserStackAuthentication browserStackAuthentication) {
        this.authorizationHeader = "Basic " + Base64.getEncoder().encodeToString(
                (browserStackAuthentication.getUserName() + ":" + browserStackAuthentication.getApiKey()).getBytes());
    }

    List<Browser> getBrowsers() {
        List<Browser> browsers = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BROWSERS_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorizationHeader)
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            browsers = objectMapper.readValue(response.body(), new TypeReference<List<Browser>>() {
            });
        } catch (InterruptedException | IOException | URISyntaxException e) {
            log.error("Couldn't retrieve browser list.", e);
        }
        return browsers;
    }

}
