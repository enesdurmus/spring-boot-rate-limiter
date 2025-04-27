package com.endu.throttler;

import java.io.Serializable;
import java.time.Instant;

public interface RateLimitState extends Serializable {

    Instant updatedAt();

}
