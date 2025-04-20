package com.endu.throttler;

public interface RateLimiterRepository {

    void save(String key, RateLimitState value);

    RateLimitState obtain(String key);

    void delete(String key);

    boolean exists(String key);
}
