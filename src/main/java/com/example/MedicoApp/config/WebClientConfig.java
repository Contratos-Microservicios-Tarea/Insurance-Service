package com.example.MedicoApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value(
            "${LICENSES_URL}"
    )
    private String licenseUrl;

    @Bean
    public WebClient licenciasWebClient() {
        return WebClient.builder()
                .baseUrl(licenseUrl)
                .build();
    }
}