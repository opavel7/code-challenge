package io.coremaker.codechallenge.dto.external.openmeteo;

import lombok.Builder;

@Builder
public record CityWeatherRequest(
        String lat,
        String lon
) {
}
