package com.endu.throttler.repository;

import org.springframework.stereotype.Component;

@Component
public class RateLimiterRepositoryFactory {

    public RateLimiterRepository get(RepositoryType type) {
        return switch (type) {
            case IN_MEMORY -> new InMemoryRateLimiterRepository();
            case REDIS -> new RedisRateLimiterRepository();
            default -> throw new IllegalArgumentException("Unsupported repository type: " + type);
        };
    }
}
