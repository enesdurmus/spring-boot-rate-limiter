package com.endu.throttler.starter;

import com.endu.throttler.inmemory.ThrottlerInMemoryAutoConfiguration;
import com.endu.throttler.redis.ThrottlerRedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ThrottlerRedisAutoConfiguration.class, ThrottlerInMemoryAutoConfiguration.class})
class ThrottlerStarterAutoConfiguration {

}
