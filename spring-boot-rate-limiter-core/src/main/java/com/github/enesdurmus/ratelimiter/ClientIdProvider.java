package com.github.enesdurmus.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;

public interface ClientIdProvider {
    String getClientId(HttpServletRequest request);
}
