package com.github.enesdurmus.ratelimiter.starter;

import com.github.enesdurmus.ratelimiter.inmemory.RateLimiterInMemoryAutoConfiguration;
import com.github.enesdurmus.ratelimiter.redis.RateLimiterRedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RateLimiterRedisAutoConfiguration.class, RateLimiterInMemoryAutoConfiguration.class})
class RateLimiterStarterAutoConfiguration {

}
