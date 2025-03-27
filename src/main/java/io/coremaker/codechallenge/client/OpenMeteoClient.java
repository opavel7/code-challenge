package io.coremaker.codechallenge.client;

import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherRequest;
import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OpenMeteoClient extends AbstractApiClient {
    private final String CLASS_NAME = OpenMeteoClient.class.getSimpleName();

    protected OpenMeteoClient(@Qualifier("openMeteoApiClient") WebClient webClient) {
        super(webClient);
    }

    public Mono<CityWeatherResponse> getCityWeather(CityWeatherRequest request) {
        var endpoint = UriComponentsBuilder.fromPath("/v1/forecast")
                .queryParam("latitude", request.lat())
                .queryParam("longitude", request.lon())
                .queryParam("current_weather", true)
                .toUriString();

        log.info("[{}] Requesting city weather for coordinates '{}'. ", CLASS_NAME, request);
        return get(endpoint, CityWeatherResponse.class)
                .doOnSuccess(response ->
                        log.info("[{}] City weather retrieved successfully for coordinates '{}'", CLASS_NAME, request));
    }
}
