package com.sipsync.sipsync.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class EmailVerification {
    @Column(name = "is_verified")
    private Boolean isVerified;

    @Id
    private Long id;

    public void setId(Long id){ this.id = id; }
    public Long getId(){ return id; }
}
