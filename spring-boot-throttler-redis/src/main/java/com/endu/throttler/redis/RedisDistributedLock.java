package com.endu.throttler.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

class RedisDistributedLock {

    private final RedisTemplate<String, String> redisTemplate;

    RedisDistributedLock(RedisTemplate<String, String> redisTemplate) {
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
