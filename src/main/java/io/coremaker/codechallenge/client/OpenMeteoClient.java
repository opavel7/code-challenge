package io.coremaker.codechallenge.client;

import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherRequest;
import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherResponse;
import io.coremaker.codechallenge.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
public class OpenMeteoClient extends AbstractApiClient {
    private final String CLASS_NAME = OpenMeteoClient.class.getSimpleName();

    @Value("${api.external.open-meteo.url}")
    private String openMeteoUrl;

    protected OpenMeteoClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return openMeteoUrl;
    }

    public CityWeatherResponse getCityWeather(CityWeatherRequest request) {
        log.info("[{}] Requesting city weather from open-meteo... ", CLASS_NAME);
        return get("/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true",
                CityWeatherResponse.class,
                Collections.emptyMap(),
                request.lat(), request.lon()
        ).orElseThrow(() -> new ExternalApiException("Weather data not found", null));
    }
}
