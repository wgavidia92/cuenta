package com.devsu.cuenta.adapter.out.integration.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class IntegrationConfiguration {

    @Value("${integration.clientes.url}")
    private String clienteUrl;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(clienteUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
