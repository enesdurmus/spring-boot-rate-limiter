package com.endu.throttler;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class RateLimiterFactory {

    private final Map<RateLimitAlgorithm, RateLimiterProvider> providerMap;

    public RateLimiterFactory(List<RateLimiterProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(RateLimiterProvider::getAlgorithm, Function.identity()));
    }


    RateLimiter create(RateLimitAlgorithm algorithm) {
        RateLimiterProvider provider = providerMap.get(algorithm);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
        return provider.create();
    }
}
