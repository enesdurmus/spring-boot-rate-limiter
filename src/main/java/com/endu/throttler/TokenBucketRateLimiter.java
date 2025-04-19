package com.endu.throttler;

import com.endu.throttler.repository.RateLimiterRepository;
import org.springframework.stereotype.Component;

@Component
class TokenBucketRateLimiter implements RateLimiter {

    private final RateLimiterRepository repository;

    TokenBucketRateLimiter(RateLimiterRepository repository) {
        this.repository = repository;
    }
}
