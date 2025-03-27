package io.coremaker.codechallenge.controller;

import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import io.coremaker.codechallenge.service.CityWeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static io.coremaker.codechallenge.Util.USER_ID_HEADER;
import static io.coremaker.codechallenge.Util.getCityWeatherUrl;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private CityWeatherService cityWeatherService;

    @Value("${resilience4j.ratelimiter.instances.userRateLimiter.limit-for-period:5}")
    private Integer limitForPeriod;

    @BeforeEach
    public void beforeEach() {
        var mockedWeather = mock(CityWeatherDTO.class);

        when(cityWeatherService.getCityWeather(any())).thenReturn(Mono.just(mockedWeather));

        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Nested
    class RateLimit {
        @Test
        public void should_throwRateLimitException_whenLimitExceeded() {
            // given
            var city = "London";
            var userId = UUID.randomUUID().toString();

            Flux.range(1, limitForPeriod)
                    .flatMap(i -> webTestClient.get()
                            .uri(getCityWeatherUrl(city))
                            .header(USER_ID_HEADER, userId)
                            .exchange()
                            .expectStatus().isOk()
                            .returnResult(CityWeatherDTO.class)
                            .getResponseBody()
                    )
                    .collectList()
                    .block();

            // when & then
            webTestClient.get()
                    .uri(getCityWeatherUrl(city))
                    .header(USER_ID_HEADER, userId)
                    .exchange()
                    .expectStatus().is4xxClientError()
                    .returnResult(CityWeatherDTO.class)
                    .getResponseBody().collectList().block();
        }
    }
}