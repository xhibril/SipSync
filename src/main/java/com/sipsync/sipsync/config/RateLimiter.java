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
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter extends OncePerRequestFilter {
    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();

    private final AuthService authService;
    public RateLimiter(AuthService authService){
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain       // let req continue
    ) throws ServletException, IOException {

        // get curr authenticated user
        Long userId = authService.getAuthenticatedUserId(request);
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();

        // auth endpoints
        for(AuthConfig ac : AUTH_CONFIGS){
            if (path.startsWith(ac.endpoint)) {
                if(!authActions(ip, ac)){
                    response.setStatus(429);
                    return;
                }
                break;
            }
        }

        // normal actions
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


    // 120 limit for normal actions
    private boolean normalActions(Long userId) {
        long key = Objects.hash(userId, "normal");

                Bucket bucket = buckets.computeIfAbsent(key, k ->
                        Bucket4j.builder()
                                .addLimit(Bandwidth.simple(120, Duration.ofMinutes(1)))
                                .build()
                );
                return bucket.tryConsume(1);
    }



    private boolean authActions(String ip, AuthConfig ac){
        long key = Objects.hash(ip, ac.key);

        Bucket bucket = buckets.computeIfAbsent(key, k ->
                Bucket4j.builder()
                        .addLimit(Bandwidth.simple(ac.perMin, Duration.ofMinutes(1)))
                        .addLimit(Bandwidth.simple(ac.perHour, Duration.ofHours(1)))
                        .build()
        );
        return bucket.tryConsume(1);
    }

    private record AuthConfig(
            String endpoint,
            String key,
            int perMin,
            int perHour
    ){}

    private static final AuthConfig[] AUTH_CONFIGS = {
            new AuthConfig(Endpoints.LOGIN, "login", 10, 25),
            new AuthConfig(Endpoints.SIGNUP, "signup", 3, 5),
            new AuthConfig(Endpoints.PASSWORD_RESET_EMAIL, "resetEmail", 2, 6),
            new AuthConfig(Endpoints.PASSWORD_RESET_CODE, "resetCode", 20, 25),
            new AuthConfig(Endpoints.PASSWORD_RESET_CHANGE, "resetChange", 20, 25),
            new AuthConfig(Endpoints.VERIFICATION_EMAIL, "verificationEmail", 2, 4)
    };
}




