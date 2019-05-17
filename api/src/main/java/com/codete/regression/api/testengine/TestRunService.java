package com.codete.regression.api.testengine;

import com.codete.regression.api.ServerConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
public class TestRunService {

    private static final String SCREENSHOTS_COMPARATOR_ENDPOINT = "/test-runs/run-screenshots-comparison";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ServerConnection serverConnection;

    public TestRunService(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public boolean compareImagesWithTheBaseline(TestRunRequest testRunRequest) {
        boolean comparisonResult = false;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String body = objectMapper.writeValueAsString(testRunRequest);
            String responseBody = serverConnection.executeHttpPost(httpClient, body, SCREENSHOTS_COMPARATOR_ENDPOINT);
            comparisonResult = Boolean.parseBoolean(responseBody);
        } catch (IOException e) {
            log.error("Cannot send screenshots to the server. {}", e.getMessage());
        }
        return comparisonResult;
    }
}
