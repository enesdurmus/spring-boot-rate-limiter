package com.github.enesdurmus.ratelimiter;

import java.util.Optional;

public interface RateLimiterRepository {

    void save(String key, RateLimitState value);

    Optional<RateLimitState> get(String key);

    void delete(String key);

    boolean exists(String key);
}
