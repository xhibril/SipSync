package com.sipsync.sipsync.model;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

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
    private String verification_token;







    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {

    }




    @PostPersist
    public void generateTokenAfterSave() {

        String secret = System.getenv("JWT_SECRET");
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

         this.verification_token = Jwts.builder()
                .setSubject(String.valueOf(this.id))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(key)
                .compact();
    }





    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setPassword(String password){ this.password = password; }
    public String getPassword(){ return password; }

    public void setId(Long id){ this.id = id; }
    public Long getId(){ return id; }

    public void setToken(String token){ this.verification_token = token; }
    public String getToken(){ return verification_token; }

}
