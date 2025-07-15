package com.endu.throttler;

import jakarta.servlet.http.HttpServletRequest;

public interface ClientIdProvider {
    String getClientId(HttpServletRequest request);
}
