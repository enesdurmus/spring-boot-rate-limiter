package com.endu.throttler.redis;

import com.endu.throttler.RateLimitState;
import com.endu.throttler.RateLimiterRepository;
import org.springframework.stereotype.Repository;

@Repository
class RedisRateLimiterRepository implements RateLimiterRepository {

    @Override
    public void save(String key, RateLimitState state) {

    }

    @Override
    public RateLimitState obtain(String key) {
        return null;
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public boolean exists(String key) {
        return false;
    }

}
