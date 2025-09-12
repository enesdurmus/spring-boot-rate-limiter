package com.github.enesdurmus.ratelimiter.inmemory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "rate-limiter.store-type", havingValue = "in-memory")
public class RateLimiterInMemoryAutoConfiguration {

    @Bean
    InMemoryRateLimiterRepository inMemoryRateLimiterRepository(
            @Value("${rate-limiter.repository.in-memory.expire-after-access:PT10M}") Duration expireAfterAccess,
            @Value("${rate-limiter.repository.in-memory.maximum-size:10000}") int maximumSize
    ) {
        return new InMemoryRateLimiterRepository(expireAfterAccess, maximumSize);
    }
}
