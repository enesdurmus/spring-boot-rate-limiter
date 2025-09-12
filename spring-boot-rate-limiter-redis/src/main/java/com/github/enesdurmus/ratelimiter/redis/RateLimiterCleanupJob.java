package com.github.enesdurmus.ratelimiter.redis;

import com.github.enesdurmus.ratelimiter.RateLimitState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class RateLimiterCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterCleanupJob.class);

    private static final String LOCK_KEY = "rate-limiter:cleanup";
    private static final long INACTIVITY_THRESHOLD_MILLIS = Duration.ofMinutes(30).toMillis();
    private static final int DELETE_BATCH_SIZE = 500;
    private static final int SCAN_BATCH_SIZE = 100;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisDistributedLock lockService;

    RateLimiterCleanupJob(RedisTemplate<String, String> redisTemplate,
                          RedisDistributedLock lockService) {
        this.redisTemplate = redisTemplate;
        this.lockService = lockService;
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void clean() {
        if (!lockService.acquireLock(LOCK_KEY, LOCK_DURATION)) {
            return;
        }

        final String hashKey = RedisRateLimiterRepository.HASH_KEY;

        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash()
                .scan(hashKey, ScanOptions.scanOptions().count(SCAN_BATCH_SIZE).build())) {

            long now = System.currentTimeMillis();
            List<Object> inactiveStates = new ArrayList<>();

            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                String key = (String) entry.getKey();
                RateLimitState state = (RateLimitState) entry.getValue();

                boolean isInactive = now - state.updatedAt().toEpochMilli() > INACTIVITY_THRESHOLD_MILLIS;

                if (isInactive) {
                    inactiveStates.add(key);
                }

                if (inactiveStates.size() >= DELETE_BATCH_SIZE) {
                    redisTemplate.opsForHash().delete(hashKey, inactiveStates.toArray());
                    inactiveStates.clear();
                }
            }

            if (!inactiveStates.isEmpty()) {
                redisTemplate.opsForHash().delete(hashKey, inactiveStates.toArray());
            }

        } catch (Exception e) {
            log.error("Failed to clean up inactive users: {}", e.getMessage());
        } finally {
            lockService.releaseLock(LOCK_KEY);
        }
    }

}
