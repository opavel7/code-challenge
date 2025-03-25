package io.coremaker.codechallenge.dto.external.openmeteo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityWeatherResponse {
    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {
        @JsonProperty("temperature")
        private Double temperature;
        @JsonProperty("windspeed")
        private Double windSpeed;
        @JsonProperty("winddirection")
        private Integer windDirection;
    }
}
