package com.sipsync.sipsync.model;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.*;

import javax.crypto.SecretKey;
import java.util.Date;

@Entity
@Table(name  = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String email;
    private String password;
    private int streak;

    @Column (name = "is_verified")
    private Boolean isVerified;

    @Column(name = "last_streak_update_date")
    private String lastStreakUpdateDate;


    public User() {}

    public User(String email, String password, String lastStreakUpdateDate) {
        this.email = email;
        this.password = password;
        this.lastStreakUpdateDate = lastStreakUpdateDate;
        this.isVerified = false;
    }




    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setPassword(String password){ this.password = password; }
    public String getPassword(){ return password; }

    public void setId(Long id){ this.id = id; }
    public Long getId(){ return id; }

    public void setStreak(int streak){ this.streak = streak; }
    public int getStreak(){ return streak; }

    public void setLastStreakUpdateDate(String date){ this.lastStreakUpdateDate = date; }
    public String getLastStreakUpdateDate(){ return lastStreakUpdateDate; }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
