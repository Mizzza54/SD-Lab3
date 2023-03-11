package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * @author Michael Gerasimov
 */
public class StockClient {
    private final String apiUrl;
    private final RestTemplate restTemplate;

    public StockClient(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    public int getStockCount(String name) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .pathSegment("stock-count")
                .queryParam("name", name)
                .build()
                .toUri();

        Integer response = restTemplate.getForObject(uri, Integer.class);

        if (response == null) {
            throw new RuntimeException();
        }
        return response;
    }

    public int getStockPrice(String name) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .pathSegment("stock-price")
                .queryParam("name", name)
                .build()
                .toUri();

        Integer response = restTemplate.getForObject(uri, Integer.class);

        if (response == null) {
            throw new RuntimeException();
        }
        return response;
    }

    public boolean buyStock(String name) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .pathSegment("buy-stock")
                .queryParam("name", name)
                .build()
                .toUri();

        HttpStatus statusCode = restTemplate.getForEntity(uri, Void.class).getStatusCode();
        return statusCode == HttpStatus.OK;
    }

    public boolean sellStock(String name) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .pathSegment("sell-stock")
                .queryParam("name", name)
                .build()
                .toUri();

        HttpStatus statusCode = restTemplate.getForEntity(uri, Void.class).getStatusCode();
        return statusCode == HttpStatus.OK;
    }
}
