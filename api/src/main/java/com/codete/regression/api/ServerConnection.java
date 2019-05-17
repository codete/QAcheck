package com.codete.regression.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

@Slf4j
public class ServerConnection {
    private final BasicResponseHandler basicResponseHandler = new BasicResponseHandler();
    private final String serverUrl;
    private final String apiKey;

    public ServerConnection(String serverUrl, String apiKey) {
        this.serverUrl = serverUrl;
        this.apiKey = apiKey;
    }

    public String executeHttpPost(CloseableHttpClient httpClient, String body, String endpointUrl)
            throws IOException {
        HttpPost httpPost = new HttpPost(serverUrl + endpointUrl);
        return executeHttpRequest(httpPost, httpClient, body);
    }

    public String executeHttpPut(CloseableHttpClient httpClient, String body, String endpointUrl)
            throws IOException {
        HttpPut httpPut = new HttpPut(serverUrl + endpointUrl);
        return executeHttpRequest(httpPut, httpClient, body);
    }

    private String executeHttpRequest(HttpEntityEnclosingRequestBase httpRequest, CloseableHttpClient httpClient,
                                      String body) throws IOException {
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("API-key", apiKey);
        httpRequest.setEntity(new StringEntity(body));
        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
            return basicResponseHandler.handleResponse(response);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                log.error("Wrong API key.");
            }
            throw e;
        }
    }
}
