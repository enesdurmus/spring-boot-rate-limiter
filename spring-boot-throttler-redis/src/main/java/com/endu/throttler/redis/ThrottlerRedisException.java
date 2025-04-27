package com.endu.throttler.redis;

public class ThrottlerRedisException extends RuntimeException {
    public ThrottlerRedisException(String message) {
        super(message);
    }
}
