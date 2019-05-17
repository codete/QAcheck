package com.codete.regression.api.testgenerator;

import com.codete.regression.api.ServerConnection;
import com.codete.regression.api.screenshot.IgnoreAreaDto;
import com.codete.regression.api.screenshot.Screenshot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class DynamicAreasService {

    private static final String DETECT_DYNAMIC_AREAS_ENDPOINT = "/dynamic-areas/detect";
    private final ServerConnection serverConnection;
    private final ObjectMapper objectMapper = new ObjectMapper();

    DynamicAreasService(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    List<IgnoreAreaDto> detectDynamicAreas(List<Screenshot> screenshots) {
        log.info("Detecting dynamic areas.");
        List<IgnoreAreaDto> ignoreAreas = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String body = objectMapper.writeValueAsString(screenshots);
            String responseBody = serverConnection.executeHttpPut(httpClient, body, DETECT_DYNAMIC_AREAS_ENDPOINT);
            ignoreAreas = objectMapper.readValue(responseBody, new TypeReference<List<IgnoreAreaDto>>() {
            });
        } catch (IOException e) {
            log.error("Cannot send request to the server. {}", e.getMessage());
        }
        return ignoreAreas;
    }
}
