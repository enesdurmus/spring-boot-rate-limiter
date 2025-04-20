package com.endu.throttler;

import com.endu.throttler.tokenbucket.TokenBucketRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class RateLimiterFactory {

    private final RateLimiterRepository repository;
    private final int capacity;
    private final long refillTokensPerSecond;

    RateLimiterFactory(RateLimiterRepository repository,
                       @Value("${throttler.token-bucket.capacity:100}") int capacity,
                       @Value("${throttler.token-bucket.refill-tokens-per-second:100}") long refillTokensPerSecond) {
        this.repository = repository;
        this.capacity = capacity;
        this.refillTokensPerSecond = refillTokensPerSecond;
    }

    RateLimiter create(RateLimitAlgorithm algorithm) {
        switch (algorithm) {
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(repository, capacity, refillTokensPerSecond);
            default:
                throw new IllegalArgumentException("Unsupported rate limit algorithm: " + algorithm);
        }
    }
}
