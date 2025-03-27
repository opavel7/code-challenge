package io.coremaker.codechallenge.client;

import io.coremaker.codechallenge.dto.external.nominatim.CityCoordinatesResponse;
import io.coremaker.codechallenge.exception.CityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NominatimClient extends AbstractApiClient {
    private final String CLASS_NAME = NominatimClient.class.getSimpleName();

    protected NominatimClient(@Qualifier("nominatimApiClient") WebClient webClient) {
        super(webClient);
    }

    public Mono<CityCoordinatesResponse> getCityCoordinates(String city) {
        var uri = UriComponentsBuilder.fromPath("/search")
                .queryParam("q", city)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();

        log.info("[{}] Requesting city coordinates for '{}'... ", CLASS_NAME, city);
        return getList(uri, CityCoordinatesResponse.class)
                .next()
                .switchIfEmpty(Mono.error(new CityNotFoundException(city)))
                .doOnSuccess(response ->
                        log.info("[{}] City coordinates retrieved successfully for '{}'. Response: {}", CLASS_NAME, city, response))
                .doOnError(throwable ->
                        log.error("[{}] Error retrieving city coordinates for '{}'. Error: {}", CLASS_NAME, city, throwable.getMessage(), throwable));

    }
}
