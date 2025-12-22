package com.sipsync.sipsync.config;
import com.sipsync.sipsync.service.AuthService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter extends OncePerRequestFilter {
    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();

    @Autowired private AuthService authService;
    @Autowired private AuthRateLimiter auth;

    // runs every http req before controllers
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,   // incoming request
            HttpServletResponse response, // outgoing response
            FilterChain filterChain       // lets the request continue
    ) throws ServletException, IOException {

        // get curr authenticated user
        Long userId = authService.getAuthenticatedUserId(request);

        String ip = null;
        if (userId == null) {
            // get the ip if user id is null
             ip = request.getRemoteAddr();
        }

        String path = request.getRequestURI();

        if (path.startsWith(Endpoints.LOGIN)) {
            if (!auth.limiter(ip, AuthRateLimiter.AuthType.LOGIN)) {
                response.setStatus(429);
                return;
            }
        }
        else if (path.startsWith(Endpoints.SIGNUP)) {
            if (!auth.limiter(ip, AuthRateLimiter.AuthType.SIGNUP)) {
                response.setStatus(429);
                return;
            }
        }
        else if (path.startsWith(Endpoints.PASSWORD_RESET)) {
            if (!auth.limiter(ip, AuthRateLimiter.AuthType.PASSWORD_RESET)) {
                response.setStatus(429);
                return;
            }
        }
        else if (path.startsWith(Endpoints.VERIFICATION_EMAIL)) {
            if (!auth.limiter(ip, AuthRateLimiter.AuthType.VERIFICATION_EMAIL)) {
                response.setStatus(429);
                return;
            }
        }



        if ((userId != null && path.startsWith("/logs"))
                        || path.startsWith("/goal")
                        || path.startsWith("/streak")
                        || path.startsWith("/stats")) {

            if(!normalActions(userId)){
                response.setStatus(429);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }



    // make the limit 120/min for normal actions
    private boolean normalActions(Long userId){
        long key = Objects.hash(userId, "normal");
        Bucket bucket = buckets.computeIfAbsent(key, k ->
                Bucket4j.builder()
                        .addLimit(Bandwidth.simple(120, Duration.ofMinutes(1)))
                        .build()
        );
        return bucket.tryConsume(1);
    }
}




