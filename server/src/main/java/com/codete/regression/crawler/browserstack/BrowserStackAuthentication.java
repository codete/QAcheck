package com.codete.regression.crawler.browserstack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class BrowserStackAuthentication {

    private final String userName;
    private final String apiKey;
    private final boolean credentialsProvided;

    BrowserStackAuthentication(Environment environment) {
        userName = environment.getProperty("crawler.browserstack.username");
        apiKey = environment.getProperty("crawler.browserstack.key");
        if (userName == null || userName.isBlank()) {
            log.warn("BrowserStack username is null. You won't be able to use browser stack integration.");
            credentialsProvided = false;
        } else if (apiKey == null || apiKey.isBlank()) {
            log.warn("BrowserStack api key is null. You won't be able to use browser stack integration.");
            credentialsProvided = false;
        } else {
            credentialsProvided = true;
        }
    }

    String getUserName() {
        return userName;
    }

    String getApiKey() {
        return apiKey;
    }

    boolean isCredentialsProvided() {
        return credentialsProvided;
    }

}
