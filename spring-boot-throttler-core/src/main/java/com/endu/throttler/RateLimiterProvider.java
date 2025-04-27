package com.endu.throttler;

public interface RateLimiterProvider {
    RateLimitAlgorithm getAlgorithm();

    RateLimiter create(RateLimiterRepository repository);

    Class<? extends RateLimitState> getStateClass();
}
