package com.endu.throttler;

import com.endu.throttler.repository.RateLimiterRepository;
import com.endu.throttler.repository.RateLimiterRepositoryFactory;
import com.endu.throttler.repository.RepositoryType;
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
    RateLimiterRepository rateLimiterRepository(@Value("${throttler.repository.type:IN_MEMORY}") String repositoryType,
                                                RateLimiterRepositoryFactory factory) {
        return factory.get(RepositoryType.valueOf(repositoryType));
    }
}
