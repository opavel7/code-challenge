package io.coremaker.codechallenge.service;

import io.coremaker.codechallenge.client.NominatimClient;
import io.coremaker.codechallenge.client.OpenMeteoClient;
import io.coremaker.codechallenge.dto.external.nominatim.CityCoordinatesResponse;
import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherResponse;
import io.coremaker.codechallenge.util.CacheNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CityWeatherServiceTest {
    @Autowired
    private CacheManager cacheManager;

    @MockitoSpyBean
    private CityWeatherService cityWeatherService;

    @MockitoBean
    private NominatimClient nominatimClient;

    @MockitoBean
    private OpenMeteoClient openMeteoClient;

    @BeforeEach
    public void beforeEach() {
        var mockCityCoordinates = mock(CityCoordinatesResponse.class);
        var mockCityWeather = mock(CityWeatherResponse.class);

        when(nominatimClient.getCityCoordinates(any())).thenReturn(Mono.just(mockCityCoordinates));
        when(openMeteoClient.getCityWeather(any())).thenReturn(Mono.just(mockCityWeather));

        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Nested
    class CacheTests {
        @Test
        void should_cacheData_whenNotCached() {
            // given
            var city = "London";
            var initialCache = cacheManager.getCache(CacheNames.CITY_WEATHER);

            assertNotNull(initialCache);
            assertNull(initialCache.get(city));

            // when
            cityWeatherService.getCityWeather(city).block();

            // then
            var cache = cacheManager.getCache(CacheNames.CITY_WEATHER);
            assertNotNull(cache);
            assertNotNull(cache.get(city));
        }

        @Test
        void should_readFromCache_whenDataIsInTheCache() {
            // given
            var city = "London";

            // when
            cityWeatherService.getCityWeather(city).block();
            cityWeatherService.getCityWeather(city).block();

            // then
            verify(cityWeatherService, times(1)).getCityWeather(any());
        }
    }
}