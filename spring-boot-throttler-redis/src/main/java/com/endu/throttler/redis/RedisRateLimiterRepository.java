package com.endu.throttler.redis;

import com.endu.throttler.RateLimitState;
import com.endu.throttler.RateLimiterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

class RedisRateLimiterRepository<T extends RateLimitState> implements RateLimiterRepository {

    static final String HASH_KEY = "throttler";

    private final HashOperations<String, String, String> hashOperations;
    private final ObjectMapper objectMapper;
    private final Class<T> typeParameterClass;

    RedisRateLimiterRepository(RedisTemplate<String, String> redisTemplate,
                               ObjectMapper objectMapper,
                               Class<T> typeParameterClass) {
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public void save(String key, RateLimitState state) {
        try {
            String json = objectMapper.writeValueAsString(state);
            hashOperations.putIfAbsent(HASH_KEY, key, json);
        } catch (Exception e) {
            throw new ThrottlerRedisException("Failed to serialize object");
        }
    }

    @Override
    public Optional<RateLimitState> get(String key) {
        String json = hashOperations.get(HASH_KEY, key);
        if (json == null) {
            return Optional.empty();
        }
        try {
            T value = objectMapper.readValue(json, typeParameterClass);
            return Optional.of(value);
        } catch (Exception e) {
            throw new ThrottlerRedisException("Failed to deserialize object");
        }
    }

    @Override
    public void delete(String key) {
        hashOperations.delete(HASH_KEY, key);
    }

    @Override
    public boolean exists(String key) {
        return hashOperations.hasKey(HASH_KEY, key);
    }

}
