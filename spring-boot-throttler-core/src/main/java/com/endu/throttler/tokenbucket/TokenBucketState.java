package com.endu.throttler.tokenbucket;

import com.endu.throttler.RateLimitState;

import java.time.Instant;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "TokenBucketState{" +
               "tokens=" + tokens +
               ", lastRefillTimestamp=" + lastRefillTimestamp +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TokenBucketState state = (TokenBucketState) o;
        return tokens == state.tokens && Objects.equals(lastRefillTimestamp, state.lastRefillTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens, lastRefillTimestamp);
    }
}
