package io.coremaker.codechallenge.exception;

public class RateLimitExceededException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Rate limit exceeded.";
    public RateLimitExceededException() {
        super(DEFAULT_MESSAGE);
    }
}
