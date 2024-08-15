package com.example.bookstore.configuration;

import com.example.bookstore.interceptor.HeaderForwardInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private HeaderForwardInterceptor headerForwardingInterceptor;

    @Bean("rest")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .additionalInterceptors(headerForwardingInterceptor)
                .build();
    }
}
