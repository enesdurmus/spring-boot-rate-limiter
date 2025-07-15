package com.endu.throttler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ThrottlerAutoConfiguration {

    @Bean
    FilterRegistrationBean<RateLimitFilter> rateLimitFilter(RateLimiter rateLimiter,
                                                            ClientIdProvider clientIdProvider) {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(rateLimiter, clientIdProvider));
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    RateLimiter rateLimiter(RateLimiterResolver rateLimiterResolver,
                            RateLimiterRepository rateLimiterRepository) {
        return rateLimiterResolver.getProvider().create(rateLimiterRepository);
    }

    @Bean
    @ConditionalOnMissingBean(ClientIdProvider.class)
    public ClientIdProvider clientIdProvider() {
        return new RemoteAddressClientIdProvider();
    }

}
