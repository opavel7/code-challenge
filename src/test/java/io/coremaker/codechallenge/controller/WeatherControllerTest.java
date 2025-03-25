package io.coremaker.codechallenge.controller;

import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import io.coremaker.codechallenge.service.CityWeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @Mock
    private CityWeatherService cityWeatherService;

    @Value("${resilience4j.ratelimiter.instances.userRateLimiter.limit-for-period:5}")
    private Integer limitForPeriod;

    @BeforeEach
    public void beforeEach() {
        var mockedWeather = mock(CityWeatherDTO.class);
        when(cityWeatherService.getCityWeather(any()))
                .thenReturn(mockedWeather);
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Test
    void should_writeCache_whenCityNotInCache() throws Exception {
        // given
        var city = "London";
        var userId = getRandomInt();
        var initialCache = cacheManager.getCache("cityWeatherCache");

        assertNotNull(initialCache);
        assertNull(initialCache.get(city));

        // when
        mockMvc.perform(get("/weather")
                        .param("city", city)
                        .header("user_id", userId))
                .andExpect(status().isOk());

        // then
        var cache = cacheManager.getCache("cityWeatherCache");
        assertNotNull(cache);
        assertNotNull(cache.get(city));
    }

    @Test
    void should_readFromCache_whenCityIsInTheCache() throws Exception {
        // given
        var city = "London";
        var userId = getRandomInt();

        // when
        mockMvc.perform(get("/weather")
                        .param("city", city)
                        .header("user_id", userId))
                .andExpect(status().isOk());

        var cache = cacheManager.getCache("cityWeatherCache");
        assertNotNull(cache);
        assertNotNull(cache.get(city));

        mockMvc.perform(get("/weather")
                        .param("city", city)
                        .header("user_id", userId))
                .andExpect(status().isOk());

        // then
        verify(cityWeatherService, never()).getCityWeather(any());
    }

    @Test
    void should_throwRateLimitException_whenLimitExceeded() throws Exception {
        // given
        var city = "London";
        var userId = getRandomInt();
        for (int i = 1; i <= limitForPeriod; i++) {
            mockMvc.perform(get("/weather")
                            .param("city", city)
                            .header("user_id", userId))
                    .andExpect(status().isOk());
        }

        // when & then
        mockMvc.perform(get("/weather")
                .param("city", city)
                .header("user_id", userId))
                .andExpect(status().isTooManyRequests());
    }

    private Integer getRandomInt() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}