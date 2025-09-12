package com.github.enesdurmus.ratelimiter.tokenbucket;

import com.github.enesdurmus.ratelimiter.RateLimiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenBucketRateLimiterTest {

    @Mock
    private RateLimiterRepository repository;

    private TokenBucketRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = new TokenBucketRateLimiter(repository, 10, 2);
    }

    @Test
    void testIsAllowed_whenStateExistsAndTokensEnough_shouldReturnTrue() {
        // given
        String key = "testKey";
        TokenBucketState state = new TokenBucketState(10, Instant.now());
        when(repository.get(key)).thenReturn(Optional.of(state));

        // when
        boolean actual = rateLimiter.isAllowed(key);

        // then
        assertThat(actual).isTrue();
        verify(repository).save(key, state);
    }

    @Test
    void testIsAllowed_whenStateExistsAndTokensNotEnough_shouldRefillAndReturnTrue() {
        // given
        String key = "testKey";
        TokenBucketState state = new TokenBucketState(0, Instant.now().minusSeconds(2));
        when(repository.get(key)).thenReturn(Optional.of(state));

        // when
        boolean actual = rateLimiter.isAllowed(key);

        // then
        assertThat(actual).isTrue();
        assertThat(state.getTokens()).isEqualTo(3);
        verify(repository).save(key, state);
    }

    @Test
    void testIsAllowed_whenStateDoesNotExist_shouldCreateNewStateAndReturnTrue() {
        // given
        String key = "newKey";
        when(repository.get(key)).thenReturn(Optional.empty());

        // when
        boolean actual = rateLimiter.isAllowed(key);

        // then
        assertThat(actual).isTrue();

        ArgumentCaptor<TokenBucketState> captor = ArgumentCaptor.forClass(TokenBucketState.class);
        verify(repository).save(eq(key), captor.capture());
        TokenBucketState savedState = captor.getValue();
        assertThat(savedState.getTokens()).isEqualTo(9);
    }

    @Test
    void testIsAllowed_whenStateExistsAndRefillExceedsCapacity_shouldNotExceedCapacity() {
        // given
        String key = "testKey";
        TokenBucketState state = new TokenBucketState(9, Instant.now().minusSeconds(5));
        when(repository.get(key)).thenReturn(Optional.of(state));

        // when
        boolean actual = rateLimiter.isAllowed(key);

        // then
        assertThat(actual).isTrue();
        assertThat(state.getTokens()).isEqualTo(9);
        verify(repository).save(key, state);
    }

    @Test
    void testIsAllowed_whenStateExistsAndTokensNotEnough_shouldReturnFalse() {
        // given
        String key = "testKey";
        TokenBucketState state = new TokenBucketState(0, Instant.now());
        when(repository.get(key)).thenReturn(Optional.of(state));

        // when
        boolean actual = rateLimiter.isAllowed(key);

        // then
        assertThat(actual).isFalse();
        verify(repository, never()).save(anyString(), any(TokenBucketState.class));
    }
}