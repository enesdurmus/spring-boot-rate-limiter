package com.github.enesdurmus.ratelimiter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    private final RateLimiter rateLimiter;
    private final ClientIdProvider clientIdProvider;

    public RateLimitFilter(RateLimiter rateLimiter,
                           ClientIdProvider clientIdProvider) {
        this.rateLimiter = rateLimiter;
        this.clientIdProvider = clientIdProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String clientId = clientIdProvider.getClientId(request);

        if (!isAllowed(clientId)) {
            response.setStatus(429);
            response.getWriter().write("Rate limit exceeded");
            log.debug("Rate limit exceeded for client: {}", clientId);
            return;
        }

        log.debug("Rate limit allowed for client: {}", clientId);
        filterChain.doFilter(request, response);
    }

    private boolean isAllowed(String clientId) {
        return rateLimiter.isAllowed(clientId);
    }
}
