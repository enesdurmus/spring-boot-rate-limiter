package com.endu.throttler.repository;

public interface RateLimiterRepository {

    void save(String key, long tokens);

    long get(String key);

    void delete(String key);

    boolean exists(String key);

    RepositoryType getType();
}
