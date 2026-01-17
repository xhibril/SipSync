package com.sipsync.sipsync.dto.auth;

public class PasswordResetRequest {
    private String email;
    private String code;
    private String password;
    private String resetToken;

    public String getResetToken(){ return resetToken; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public String getCode() {return code;}

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
