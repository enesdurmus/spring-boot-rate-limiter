package com.endu.throttler.tokenbucket;

import com.endu.throttler.RateLimiter;
import com.endu.throttler.RateLimiterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class TokenBucketRateLimiter implements RateLimiter {

    private static final Logger log = LoggerFactory.getLogger(TokenBucketRateLimiter.class);

    private final RateLimiterRepository repository;
    private final int capacity;
    private final long refillTokensPerSecond;

    public TokenBucketRateLimiter(
            RateLimiterRepository repository,
            int capacity,
            long refillTokensPerSecond) {
        this.repository = repository;
        this.capacity = capacity;
        this.refillTokensPerSecond = refillTokensPerSecond;
    }

    @Override
    public boolean isAllowed(String key) {
        TokenBucketState bucket = repository.get(key)
                .map(state -> refill((TokenBucketState) state))
                .orElse(new TokenBucketState(
                        capacity,
                        Instant.now()
                ));

        log.debug("TokenBucket state for key {} is {}", key, bucket);

        if (bucket.getTokens() >= 1) {
            bucket.setTokens(bucket.getTokens() - 1);
            repository.save(key, bucket);
            return true;
        }

        return false;
    }

    private TokenBucketState refill(TokenBucketState bucket) {
        Instant now = Instant.now();
        long secondsSinceLastRefill = Duration.between(bucket.getLastRefillTimestamp(), now).toSeconds();
        long tokensToAdd = secondsSinceLastRefill * refillTokensPerSecond;
        log.debug("Refilling {} tokens to: {}", tokensToAdd, bucket);
        if (tokensToAdd > 0) {
            bucket.setTokens(Math.min(capacity, bucket.getTokens() + tokensToAdd));
            bucket.setLastRefillTimestamp(now);
        }
        return bucket;
    }

}
