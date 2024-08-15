package com.example.bookstore.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalApiService {
    private final RestTemplate restTemplate;

    public ExternalApiService(@Qualifier("rest") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> call(String url) {
         return restTemplate.getForEntity(url, String.class);
    }
}
