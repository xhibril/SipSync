package com.sipsync.sipsync.model;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name  = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private Integer streak;

    @Column (name = "is_verified")
    private Boolean isVerified;

    @Column(name = "last_streak_update_date")
    private LocalDate lastStreakUpdateDate;

    public User(){}

    public User(String email, String password, LocalDate lastStreakUpdateDate) {
        this.email = email;
        this.password = password;
        this.streak = 0;
        this.lastStreakUpdateDate = lastStreakUpdateDate;
        this.isVerified = false;
    }

    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setPassword(String password){ this.password = password; }
    public String getPassword(){ return password; }

    public Long getId(){ return id; }

    public void setStreak(int streak){ this.streak = streak; }
    public int getStreak(){ return streak; }

    public void setLastStreakUpdateDate(LocalDate date){ this.lastStreakUpdateDate = date; }
    public LocalDate getLastStreakUpdateDate(){ return lastStreakUpdateDate; }

    public Boolean getVerified() {
        return isVerified;
    }
    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
