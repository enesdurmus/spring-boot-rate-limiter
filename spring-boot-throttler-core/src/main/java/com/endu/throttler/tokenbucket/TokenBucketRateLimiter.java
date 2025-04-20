package com.endu.throttler.tokenbucket;

import com.endu.throttler.RateLimitAlgorithm;
import com.endu.throttler.RateLimiter;
import com.endu.throttler.RateLimiterRepository;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public class TokenBucketRateLimiter implements RateLimiter {

    private final RateLimiterRepository repository;
    private final int capacity;
    private final long refillTokensPerSecond;

    public TokenBucketRateLimiter(
            RateLimiterRepository repository,
            @Value("${throttler.repository.type:IN_MEMORY}") int capacity,
            @Value("${throttler.repository.type:IN_MEMORY}") long refillTokensPerSecond) {
        this.repository = repository;
        this.capacity = capacity;
        this.refillTokensPerSecond = refillTokensPerSecond;
    }

    @Override
    public boolean isAllowed(String key) {
        TokenBucketState bucket = (TokenBucketState) repository.obtain(key);
        refill(bucket);

        if (bucket.getTokens() >= 1) {
            bucket.setTokens(bucket.getTokens() - 1);
            repository.save(key, bucket);
            return true;
        }

        return false;
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.TOKEN_BUCKET;
    }

    private void refill(TokenBucketState bucket) {
        Instant now = Instant.now();
        long secondsSinceLastRefill = (long) ((now.toEpochMilli() - bucket.getLastRefillTimestamp().toEpochMilli()) / 1000.0);
        long tokensToAdd = secondsSinceLastRefill * refillTokensPerSecond;
        bucket.setTokens(Math.min(capacity, bucket.getTokens() + tokensToAdd));
        bucket.setLastRefillTimestamp(now);
    }
}
