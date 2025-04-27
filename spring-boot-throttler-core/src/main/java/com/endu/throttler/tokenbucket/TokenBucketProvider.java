package com.endu.throttler.tokenbucket;

import com.endu.throttler.RateLimitAlgorithm;
import com.endu.throttler.RateLimiter;
import com.endu.throttler.RateLimiterProvider;
import com.endu.throttler.RateLimiterRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class TokenBucketProvider implements RateLimiterProvider {

    private final int capacity;
    private final long refillTokensPerSecond;

    TokenBucketProvider(@Value("${throttler.token-bucket.capacity:100}") int capacity,
                        @Value("${throttler.token-bucket.refill-tokens-per-second:2}") long refillTokensPerSecond) {
        this.capacity = capacity;
        this.refillTokensPerSecond = refillTokensPerSecond;
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.TOKEN_BUCKET;
    }

    @Override
    public RateLimiter create(RateLimiterRepository repository) {
        return new TokenBucketRateLimiter(repository, capacity, refillTokensPerSecond);
    }

    @Override
    public Class<? extends TokenBucketState> getStateClass() {
        return TokenBucketState.class;
    }

}
