package com.endu.throttler.redis;

import com.endu.throttler.RateLimiterResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnBean(RedisTemplate.class)
@ConditionalOnProperty(prefix = "throttler", name = "store-type", havingValue = "redis", matchIfMissing = true)
public class ThrottlerRedisAutoConfiguration {

    @Bean
    RedisRateLimiterRepository<?> repository(RedisTemplate<String, String> redisTemplate,
                                             ObjectMapper objectMapper,
                                             RateLimiterResolver rateLimiterResolver) {


        return new RedisRateLimiterRepository<>(
                redisTemplate,
                objectMapper,
                rateLimiterResolver.getProvider().getStateClass());
    }

    @Bean
    RedisDistributedLock redisDistributedLock(RedisTemplate<String, String> redisTemplate) {
        return new RedisDistributedLock(redisTemplate);
    }

    @Bean
    RateLimiterCleanupJob rateLimiterCleanupJob(RedisTemplate<String, String> redisTemplate,
                                                RedisDistributedLock redisDistributedLock) {
        return new RateLimiterCleanupJob(redisTemplate, redisDistributedLock);
    }

}
