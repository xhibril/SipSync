package com.sipsync.sipsync.dto.auth;

public class PasswordResetResponse{
    String message;
    String resetToken;
    Integer attemptsRemaining;

    public void setResetToken(String resetToken){ this.resetToken = resetToken; }
    public String getResetToken(){ return resetToken; }

    public void setMessage(String message){ this.message = message; }
    public String getMessage(){ return message; }

    public void setAttemptsRemaining(Integer attemptsRemaining){ this.attemptsRemaining = attemptsRemaining; }
    public Integer getAttemptsRemaining(){ return attemptsRemaining; }
}

