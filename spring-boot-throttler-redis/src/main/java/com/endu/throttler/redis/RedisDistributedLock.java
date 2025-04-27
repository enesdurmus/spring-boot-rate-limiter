package com.endu.throttler.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
class RedisDistributedLock {

    private final RedisTemplate<String, Object> redisTemplate;

    RedisDistributedLock(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean acquireLock(String lockKey, Duration duration) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", duration);
        return Boolean.TRUE.equals(result);
    }

    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }

}
