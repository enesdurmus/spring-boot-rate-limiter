package com.github.enesdurmus.ratelimiter.redis;

public class RateLimiterRedisException extends RuntimeException {
    public RateLimiterRedisException(String message) {
        super(message);
    }
}
