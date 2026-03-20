package com.example.orderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${product.service.base-url:http://localhost:8080}")
    private String productServiceBaseUrl;

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(productServiceBaseUrl)
                .build();
    }
}
