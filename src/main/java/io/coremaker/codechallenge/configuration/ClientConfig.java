package io.coremaker.codechallenge.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ClientConfig {

    @Value("${api.external.nominatim.url}")
    private String nominatimUrl;

    @Value("${api.external.open-meteo.url}")
    private String openMeteoUrl;

    @Bean(name = "nominatimApiClient")
    public WebClient nominatimApiClient(WebClient.Builder webClientBuilder) {
        log.info("nominatimApiClient {}", nominatimUrl);
        return webClientBuilder
                .baseUrl(nominatimUrl)
                .build();
    }

    @Bean(name = "openMeteoApiClient")
    public WebClient openMeteoApiClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(openMeteoUrl)
                .build();
    }
}
