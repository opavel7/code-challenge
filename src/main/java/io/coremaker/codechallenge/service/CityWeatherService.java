package io.coremaker.codechallenge.service;

import io.coremaker.codechallenge.client.NominatimClient;
import io.coremaker.codechallenge.client.OpenMeteoClient;
import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherRequest;
import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import io.coremaker.codechallenge.mapper.CityWeatherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static io.coremaker.codechallenge.util.CacheNames.CITY_WEATHER;

@Service
@RequiredArgsConstructor
public class CityWeatherService {
    private final NominatimClient nominatimClient;
    private final OpenMeteoClient openMeteoClient;
    private final CityWeatherMapper cityWeatherMapper;

    @Cacheable(value = CITY_WEATHER, key = "#city")
    public CityWeatherDTO getCityWeather(String city) {
        var cityCoordinates = nominatimClient.getCityCoordinates(city);
        var request = CityWeatherRequest.builder()
                .lat(cityCoordinates.lat())
                .lon(cityCoordinates.lon())
                .build();
        var cityWeather = openMeteoClient.getCityWeather(request);

        return cityWeatherMapper.toCityWeatherDTO(cityWeather);
    }
}
