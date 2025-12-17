package com.sipsync.sipsync.dto.auth;

public record PasswordResetErrorResponse(
        String message,
        Integer attemptsRemaining
){}
