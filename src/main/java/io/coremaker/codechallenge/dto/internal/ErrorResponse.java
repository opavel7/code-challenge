package io.coremaker.codechallenge.dto.internal;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        Integer status,
        String message,
        String details,
        Instant timestamp
) {
}
