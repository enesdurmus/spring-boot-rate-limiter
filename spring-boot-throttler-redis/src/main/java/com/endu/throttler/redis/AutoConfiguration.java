package com.endu.throttler.redis;

import com.endu.throttler.RateLimiterResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
class AutoConfiguration {

    @Bean
    RedisRateLimiterRepository<?> repository(RedisTemplate<String, String> redisTemplate,
                                             ObjectMapper objectMapper,
                                             RateLimiterResolver rateLimiterResolver) {


        return new RedisRateLimiterRepository<>(
                redisTemplate,
                objectMapper,
                rateLimiterResolver.getProvider().getStateClass());
    }

}
