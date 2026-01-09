package com.sipsync.sipsync.model;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "password_reset_requests")
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "code_hash")
    private String code;

    @Column(name = "expires_at")
    private Instant codeExpiration;

    @Column(name = "attempts_remaining")
    private Integer attemptsRemaining;

    @Column(name = "reset_token")
    public String resetToken;

    @Column(name = "reset_token_expires_at")
    public Instant resetTokenExpiration;

    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setCode(String code){ this.code = code; }
    public String getCode(){ return code; }

    public Instant getCodeExpiration() {
        return codeExpiration;
    }

    public void setCodeExpiration(Instant codeExpiration) {
        this.codeExpiration = codeExpiration;
    }

    public Integer getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public void setAttemptsRemaining(Integer attemptsRemaining) {
        this.attemptsRemaining = attemptsRemaining;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResetToken() {
        return resetToken;
    }
}
