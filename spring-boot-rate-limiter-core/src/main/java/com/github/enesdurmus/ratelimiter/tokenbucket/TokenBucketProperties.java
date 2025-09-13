package com.github.enesdurmus.ratelimiter.tokenbucket;

public class TokenBucketProperties {
    private int capacity;
    private int refillTokensPerSecond;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRefillTokensPerSecond() {
        return refillTokensPerSecond;
    }

    public void setRefillTokensPerSecond(int refillTokensPerSecond) {
        this.refillTokensPerSecond = refillTokensPerSecond;
    }
}
