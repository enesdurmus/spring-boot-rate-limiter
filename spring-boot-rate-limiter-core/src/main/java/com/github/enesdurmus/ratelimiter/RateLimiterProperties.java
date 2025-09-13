package com.github.enesdurmus.ratelimiter;

import com.github.enesdurmus.ratelimiter.tokenbucket.TokenBucketProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterProperties {
    private RateLimitAlgorithm algorithm = RateLimitAlgorithm.TOKEN_BUCKET;
    private List<String> excludePaths;
    private String storeType;
    private TokenBucketProperties tokenBucket;

    public List<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public TokenBucketProperties getTokenBucket() {
        return tokenBucket;
    }

    public void setTokenBucket(TokenBucketProperties tokenBucket) {
        this.tokenBucket = tokenBucket;
    }

    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(RateLimitAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
}