package com.endu.throttler.tokenbucket;

import com.endu.throttler.RateLimitState;

import java.time.Instant;

class TokenBucketState implements RateLimitState {

    private long tokens;
    private Instant lastRefillTimestamp;

    TokenBucketState(long tokens, Instant lastRefillTimestamp) {
        this.tokens = tokens;
        this.lastRefillTimestamp = lastRefillTimestamp;
    }

    public Instant getLastRefillTimestamp() {
        return lastRefillTimestamp;
    }

    public void setLastRefillTimestamp(Instant lastRefillTimestamp) {
        this.lastRefillTimestamp = lastRefillTimestamp;
    }

    public long getTokens() {
        return tokens;
    }

    public void setTokens(long tokens) {
        this.tokens = tokens;
    }
}
