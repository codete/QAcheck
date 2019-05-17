package com.codete.regression.api.testgenerator.imagecomparator;

import com.codete.regression.api.ServerConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
public class ImageComparatorService {

    private static final String COMPARE_IMAGES_ENDPOINT = "/image-comparator/run-comparison";
    private final ServerConnection serverConnection;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ImageComparatorService(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public ImageComparatorResponse compareImages(ImageComparatorRequest imageComparatorRequest) {
        ImageComparatorResponse imageComparatorResponse = new ImageComparatorResponse();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String body = objectMapper.writeValueAsString(imageComparatorRequest);
            String responseBody = serverConnection.executeHttpPut(httpClient, body, COMPARE_IMAGES_ENDPOINT);
            imageComparatorResponse = objectMapper.readValue(responseBody, ImageComparatorResponse.class);
        } catch (IOException e) {
            log.error("Cannot send request to the server. {}", e.getMessage());
        }
        return imageComparatorResponse;
    }
}
