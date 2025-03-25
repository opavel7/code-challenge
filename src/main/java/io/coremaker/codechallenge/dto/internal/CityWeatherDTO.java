package io.coremaker.codechallenge.dto.internal;

import lombok.Builder;

@Builder
public record CityWeatherDTO(
        Double temperature,
        Double windSpeed,
        Integer windDirection
) {
}
