package com.sipsync.sipsync.model;

public record PasswordResetErrorResponse(
        String message,
        Integer attemptsRemaining
){}
