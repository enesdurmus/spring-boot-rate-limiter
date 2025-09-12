package com.github.enesdurmus.ratelimiter.tokenbucket;

import com.github.enesdurmus.ratelimiter.RateLimitAlgorithm;
import com.github.enesdurmus.ratelimiter.RateLimiter;
import com.github.enesdurmus.ratelimiter.RateLimiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TokenBucketProviderTest {

    private TokenBucketProvider provider;

    @BeforeEach
    void setUp() {
        provider = new TokenBucketProvider(5, 2);
    }

    @Test
    void testGetAlgorithm() {
        // when
        RateLimitAlgorithm actual = provider.getAlgorithm();

        // then
        assertThat(actual).isEqualTo(RateLimitAlgorithm.TOKEN_BUCKET);
    }

    @Test
    void testCreate() {
        // given
        RateLimiterRepository repository = mock(RateLimiterRepository.class);

        // when
        RateLimiter actual = provider.create(repository);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void testGetStateClass() {
        // when
        Class<? extends TokenBucketState> actual = provider.getStateClass();

        // then
        assertThat(actual).isEqualTo(TokenBucketState.class);
    }

}