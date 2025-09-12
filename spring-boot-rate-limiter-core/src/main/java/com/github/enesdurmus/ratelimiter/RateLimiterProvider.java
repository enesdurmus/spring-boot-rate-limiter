package com.github.enesdurmus.ratelimiter;

public interface RateLimiterProvider {
    RateLimitAlgorithm getAlgorithm();

    RateLimiter create(RateLimiterRepository repository);

    Class<? extends RateLimitState> getStateClass();
}
