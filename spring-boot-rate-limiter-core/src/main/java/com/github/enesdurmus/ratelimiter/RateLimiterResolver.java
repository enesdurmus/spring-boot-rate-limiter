package com.github.enesdurmus.ratelimiter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RateLimiterResolver {

    private final Map<RateLimitAlgorithm, RateLimiterProvider> providerMap;
    private final RateLimitAlgorithm algorithm;

    public RateLimiterResolver(List<RateLimiterProvider> providers,
                               RateLimiterProperties properties) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(RateLimiterProvider::getAlgorithm, Function.identity()));
        this.algorithm = properties.getAlgorithm();
    }

    public RateLimiterProvider getProvider() {
        return providerMap.get(algorithm);
    }

}