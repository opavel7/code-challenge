package io.coremaker.codechallenge.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ClientConfig {
    @Bean(name = "nominatimApiClient")
    public WebClient nominatimApiClient(
            WebClient.Builder webClientBuilder,
            @Value("${api.external.nominatim.url}") String nominatimUrl) {
        return webClientBuilder
                .baseUrl(nominatimUrl)
                .build();
    }

    @Bean(name = "openMeteoApiClient")
    public WebClient openMeteoApiClient(
            WebClient.Builder webClientBuilder,
            @Value("${api.external.open-meteo.url}") String openMeteoUrl) {
        return webClientBuilder
                .baseUrl(openMeteoUrl)
                .build();
    }
}
