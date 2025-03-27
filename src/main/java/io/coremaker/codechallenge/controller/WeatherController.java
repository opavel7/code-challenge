package io.coremaker.codechallenge.controller;

import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import io.coremaker.codechallenge.exception.RateLimitExceededException;
import io.coremaker.codechallenge.service.CityWeatherService;
import io.coremaker.codechallenge.service.RateLimiterService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {
    private final CityWeatherService cityWeatherService;
    private final RateLimiterService rateLimiterService;

    @GetMapping
    public Mono<CityWeatherDTO> getWeather(
            @RequestParam @NotBlank String city,
            @RequestHeader(value = "user_id") String userId) {
        if (!rateLimiterService.isRequestAllowed(userId)) {
            throw  new RateLimitExceededException();
        }

        return cityWeatherService.getCityWeather(city);
    }
}
