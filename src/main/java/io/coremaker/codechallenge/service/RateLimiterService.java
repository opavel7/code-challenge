package io.coremaker.codechallenge.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    @Value("${resilience4j.ratelimiter.instances.userRateLimiter.limit-for-period:5}")
    private Integer limitForPeriod;

    @Value("${resilience4j.ratelimiter.instances.userRateLimiter.limit-refresh-period:PT1M}")
    private String limitRefreshPeriod;

    @Value("${resilience4j.ratelimiter.instances.userRateLimiter.timeout-duration:500}")
    private Long timeoutDuration;

    private final ConcurrentHashMap<String, RateLimiter> rateLimiterCache = new ConcurrentHashMap<>();

    private RateLimiter createRateLimiter(String userId) {
        return RateLimiter.of(userId, RateLimiterConfig.custom()
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(Duration.parse(limitRefreshPeriod))
                .timeoutDuration(Duration.ofMillis(timeoutDuration))
                .build());
    }

    public Boolean isRequestAllowed(String userId) {
        RateLimiter rateLimiter = rateLimiterCache.computeIfAbsent(userId, this::createRateLimiter);
        return rateLimiter.acquirePermission();
    }

}
