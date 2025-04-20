package com.endu.throttler;

public interface RateLimiter {
    boolean isAllowed(String key);

    RateLimitAlgorithm getAlgorithm();
}
