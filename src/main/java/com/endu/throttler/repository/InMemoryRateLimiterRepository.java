package com.endu.throttler.repository;

class InMemoryRateLimiterRepository implements RateLimiterRepository {

    @Override
    public void save(String key, long tokens) {

    }

    @Override
    public long get(String key) {
        return 0;
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public boolean exists(String key) {
        return false;
    }

    @Override
    public RepositoryType getType() {
        return RepositoryType.IN_MEMORY;
    }
}
