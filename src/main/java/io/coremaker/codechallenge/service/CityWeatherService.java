package io.coremaker.codechallenge.service;

import io.coremaker.codechallenge.client.NominatimClient;
import io.coremaker.codechallenge.client.OpenMeteoClient;
import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import io.coremaker.codechallenge.mapper.CityWeatherMapper;
import io.coremaker.codechallenge.mapper.CityWeatherRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.coremaker.codechallenge.util.CacheNames.CITY_WEATHER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityWeatherService {
    public static final String CLASS_NAME = CityWeatherService.class.getSimpleName();
    private final NominatimClient nominatimClient;
    private final OpenMeteoClient openMeteoClient;
    private final CityWeatherMapper cityWeatherMapper;
    private final CityWeatherRequestMapper cityWeatherRequestMapper;

    @Cacheable(value = CITY_WEATHER, key = "#city")
    public Mono<CityWeatherDTO> getCityWeather(String city) {
        log.info("[{}] Getting city weather for '{}'...", CLASS_NAME, city);
        return nominatimClient.getCityCoordinates(city)
                .flatMap(response ->
                        openMeteoClient.getCityWeather(cityWeatherRequestMapper.toCityWeatherDTO(response)))
                .map(cityWeatherMapper::toCityWeatherDTO);
    }
}
