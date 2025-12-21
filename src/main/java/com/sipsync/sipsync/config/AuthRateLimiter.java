package com.sipsync.sipsync.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthRateLimiter {

    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean limiter(Long userId, UserRateLimitFilter.AuthType type){
        int limit, hourLimit;
        String endpoint;

        switch (type) {
            case LOGIN -> {
                limit = 5;
                hourLimit = 25;
                endpoint = "login";
            }
            case SIGNUP -> {
                limit = 3;
                hourLimit = 5;
                endpoint = "signup";
            }

            case VERIFICATION_EMAIL -> {
                limit = 1;
                hourLimit = 3;
                endpoint = "verificationEmail";
            }

            case PASSWORD_RESET -> {
                limit = 1;
                hourLimit = 3;
                endpoint = "passwordReset";
            }
            default -> {
                return false;
            }
        }

        long key = Objects.hash(userId, endpoint);     // unique key = user + endpoint
        Bucket authBucket = buckets.computeIfAbsent(key, k ->
                Bucket4j.builder()
                        .addLimit(Bandwidth.simple(limit, Duration.ofMinutes(1)))
                        .addLimit(Bandwidth.simple(hourLimit, Duration.ofHours(1)))
                        .build()
        );

        return authBucket.tryConsume(1);
    }
}


