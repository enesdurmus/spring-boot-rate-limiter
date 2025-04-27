package com.endu.throttler.inmemory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(prefix = "throttler", name = "store-type", havingValue = "in-memory")
public class ThrottlerInMemoryAutoConfiguration {

    @Bean
    InMemoryRateLimiterRepository inMemoryRateLimiterRepository(
            @Value("${throttler.repository.in-memory.expire-after-access:PT10M}") Duration expireAfterAccess,
            @Value("${throttler.repository.in-memory.maximum-size:10000}") int maximumSize
    ) {
        return new InMemoryRateLimiterRepository(expireAfterAccess, maximumSize);
    }
}
