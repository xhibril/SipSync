package com.sipsync.sipsync.config;
import com.sipsync.sipsync.service.AuthService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Bandwidth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRateLimitFilter extends OncePerRequestFilter {

    enum AuthType {
        LOGIN,
        SIGNUP,
        VERIFICATION_EMAIL,
        PASSWORD_RESET,
    }

    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRateLimiter auth;

    // runs every http req before controllers
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,   // incoming request
            HttpServletResponse response, // outgoing response
            FilterChain filterChain       // lets the request continue
    ) throws ServletException, IOException {

        // get curr authenticated user
        Long userId = authService.getAuthenticatedUserId(request);

        if (userId == null) {
            filterChain.doFilter(request, response); // let req continue
            return;
        }


        String path = request.getRequestURI();



        switch (path) {
            case Endpoints.LOGIN -> {
                if (!auth.limiter(userId, AuthType.LOGIN)) {
                    response.setStatus(429);
                    return;
                }
            }

            case Endpoints.SIGNUP -> {
                if (!auth.limiter(userId, AuthType.SIGNUP)) {
                    response.setStatus(429);
                    return;
                }
            }

            case Endpoints.PASSWORD_RESET -> {
                if (!auth.limiter(userId, AuthType.PASSWORD_RESET)) {
                    response.setStatus(429);
                }
            }

            case Endpoints.VERIFICATION_EMAIL -> {
                if (!auth.limiter(userId, AuthType.VERIFICATION_EMAIL)) {
                    response.setStatus(429);
                }
            }
        }
    }
}




