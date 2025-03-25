package io.coremaker.codechallenge.controller;

import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import io.coremaker.codechallenge.exception.RateLimitExceededException;
import io.coremaker.codechallenge.service.CityWeatherService;
import io.coremaker.codechallenge.service.RateLimiterService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {
    private final CityWeatherService cityWeatherService;
    private final RateLimiterService rateLimiterService;

    @GetMapping("/weather")
    public ResponseEntity<CityWeatherDTO> getWeather(
            @RequestParam @NotBlank String city,
            @RequestHeader(value = "user_id") String userId) {

        if (!rateLimiterService.isRequestAllowed(userId)) {
            throw  new RateLimitExceededException();
        }

        return ResponseEntity.ok(cityWeatherService.getCityWeather(city));
    }
}
