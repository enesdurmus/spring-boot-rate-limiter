package com.github.enesdurmus.ratelimiter.tokenbucket;

import com.github.enesdurmus.ratelimiter.RateLimitAlgorithm;
import com.github.enesdurmus.ratelimiter.RateLimiter;
import com.github.enesdurmus.ratelimiter.RateLimiterProperties;
import com.github.enesdurmus.ratelimiter.RateLimiterProvider;
import com.github.enesdurmus.ratelimiter.RateLimiterRepository;
import org.springframework.stereotype.Component;

@Component
class TokenBucketProvider implements RateLimiterProvider {

    private final TokenBucketProperties properties;

    TokenBucketProvider(RateLimiterProperties properties) {
        this.properties = properties.getTokenBucket();
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.TOKEN_BUCKET;
    }

    @Override
    public RateLimiter create(RateLimiterRepository repository) {
        return new TokenBucketRateLimiter(repository, properties.getCapacity(), properties.getRefillTokensPerSecond());
    }

    @Override
    public Class<? extends TokenBucketState> getStateClass() {
        return TokenBucketState.class;
    }

}
