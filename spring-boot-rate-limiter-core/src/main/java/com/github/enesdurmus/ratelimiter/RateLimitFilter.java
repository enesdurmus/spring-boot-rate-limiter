package com.github.enesdurmus.ratelimiter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.List;

public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    private final RateLimiter rateLimiter;
    private final ClientIdProvider clientIdProvider;
    private final List<PathPattern> excludePatterns;

    public RateLimitFilter(RateLimiter rateLimiter,
                           ClientIdProvider clientIdProvider,
                           List<String> excludePaths) {
        this.rateLimiter = rateLimiter;
        this.clientIdProvider = clientIdProvider;
        this.excludePatterns = excludePaths.stream()
                .map(pattern -> new PathPatternParser().parse(pattern))
                .toList();
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        PathContainer pathContainer = PathContainer.parsePath(request.getRequestURI());
        return excludePatterns.stream()
                .anyMatch(pattern -> pattern.matches(pathContainer));
    }
}
