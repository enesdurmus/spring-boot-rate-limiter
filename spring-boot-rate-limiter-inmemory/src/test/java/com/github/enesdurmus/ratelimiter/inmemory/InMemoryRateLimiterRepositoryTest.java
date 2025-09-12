package com.github.enesdurmus.ratelimiter.inmemory;

import com.github.enesdurmus.ratelimiter.RateLimitState;
import com.github.enesdurmus.ratelimiter.tokenbucket.TokenBucketState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryRateLimiterRepositoryTest {

    private InMemoryRateLimiterRepository instance;

    @BeforeEach
    void setUp() {
        Duration expireAfterAccess = Duration.ofSeconds(30);
        int maximumSize = 5;
        instance = new InMemoryRateLimiterRepository(expireAfterAccess, maximumSize);
    }

    @Test
    void testSaveAndGet() {
        // given
        String key = "testKey";
        RateLimitState state = new TokenBucketState(10, Instant.now());

        // when
        instance.save(key, state);


        // then
        Optional<RateLimitState> actual = instance.get(key);
        assertThat(actual).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(state);
    }

    @Test
    void testGetNonExistentKey() {
        // when
        Optional<RateLimitState> actual = instance.get("nonExistentKey");

        // then
        assertThat(actual).isNotPresent();
    }

    @Test
    void testDelete() {
        // given
        String key = "testKey";
        RateLimitState state = new TokenBucketState(10, Instant.now());
        instance.save(key, state);

        // when
        instance.delete(key);

        // then
        Optional<RateLimitState> actual = instance.get(key);
        assertThat(actual).isNotPresent();
    }

    @Test
    void testExists() {
        // given
        String key = "testKey";
        RateLimitState state = new TokenBucketState(10, Instant.now());
        instance.save(key, state);

        // when
        boolean exists = instance.exists(key);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsNonExistentKey() {
        // when
        boolean exists = instance.exists("nonExistentKey");

        // then
        assertThat(exists).isFalse();
    }

}