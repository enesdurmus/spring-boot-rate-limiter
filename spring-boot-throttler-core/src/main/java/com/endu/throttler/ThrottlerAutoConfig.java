package com.endu.throttler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ThrottlerAutoConfig {

    @Bean
    FilterRegistrationBean<RateLimitFilter> rateLimitFilter(RateLimiter rateLimiter) {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(rateLimiter));
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    RateLimiter rateLimiter(RateLimiterFactory factory,
                            @Value("${throttler.algorithm:TOKEN_BUCKET}") RateLimitAlgorithm algorithm) {
        return factory.create(algorithm);
    }

}
