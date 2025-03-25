package io.coremaker.codechallenge.client;

import io.coremaker.codechallenge.dto.external.nominatim.CityCoordinatesResponse;
import io.coremaker.codechallenge.exception.CityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NominatimClient extends AbstractApiClient {
    private final String CLASS_NAME = NominatimClient.class.getSimpleName();

    @Value("${api.external.nominatim.url}")
    private String nominatimUrl;

    protected NominatimClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return nominatimUrl;
    }

    public CityCoordinatesResponse getCityCoordinates(String city) {
        log.info("[{}] Requesting nominatim city coordinates...", CLASS_NAME);
        return get("/search?q={city}&format=json&limit=1",
                new ParameterizedTypeReference<List<CityCoordinatesResponse>>() {},
                Collections.emptyMap(),
                city
        ).filter(response -> !response.isEmpty())
                .map(response -> response.get(0))
                .orElseThrow(() -> new CityNotFoundException(city));
    }
}
