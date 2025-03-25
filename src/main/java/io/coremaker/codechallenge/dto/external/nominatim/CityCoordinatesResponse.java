package io.coremaker.codechallenge.dto.external.nominatim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CityCoordinatesResponse(String lat, String lon) {
}
