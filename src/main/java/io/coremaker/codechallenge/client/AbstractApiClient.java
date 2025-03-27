package io.coremaker.codechallenge.client;

import io.coremaker.codechallenge.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class AbstractApiClient {
    private final String CLASS_NAME = NominatimClient.class.getSimpleName();

    private final WebClient webClient;

    protected AbstractApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    protected <T> Mono<T> get(String endpoint, Class<T> responseType) {
        return webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(throwable -> {
                    log.error("[{}] Exception occurred while calling endpoint '{}': {}", CLASS_NAME, endpoint, throwable.getMessage(), throwable);
                    return Mono.error(new ExternalApiException("Failed to fetch data from external API", throwable));
                });
    }


    protected <T> Flux<T> getList(String endpoint, Class<T> responseType) {
        return webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToFlux(responseType)
                .onErrorResume(throwable -> {
                    log.error("[{}] Exception occurred while calling endpoint '{}': {}", CLASS_NAME, endpoint, throwable.getMessage(), throwable);
                    return Flux.error(new ExternalApiException("Failed to fetch data from external API", throwable));
                });
    }

}
