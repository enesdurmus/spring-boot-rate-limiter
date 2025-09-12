package com.github.enesdurmus.ratelimiter;

public interface RateLimiter {
    boolean isAllowed(String key);
}
