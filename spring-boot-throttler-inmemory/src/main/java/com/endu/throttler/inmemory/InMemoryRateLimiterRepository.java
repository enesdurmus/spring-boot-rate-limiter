package com.endu.throttler.inmemory;

import com.endu.throttler.RateLimitState;
import com.endu.throttler.RateLimiterRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

class InMemoryRateLimiterRepository implements RateLimiterRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryRateLimiterRepository.class);

    private final Cache<String, RateLimitState> cache;

    InMemoryRateLimiterRepository(Duration expireAfterAccess,
                                  int maximumSize) {
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(expireAfterAccess)
                .maximumSize(maximumSize)
                .build();
    }

    @Override
    public void save(String key, RateLimitState state) {
        log.debug("save key={} state={}", key, state);
        cache.put(key, state);
    }

    @Override
    public Optional<RateLimitState> get(String key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    @Override
    public void delete(String key) {
        cache.invalidate(key);
    }

    @Override
    public boolean exists(String key) {
        return cache.getIfPresent(key) != null;
    }

}
