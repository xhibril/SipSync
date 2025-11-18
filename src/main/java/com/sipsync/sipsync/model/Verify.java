package com.sipsync.sipsync.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Verify {



    @Column(name = "is_verified")
    private Boolean isVerified;

    @Id
    private Long id;

    public Verify() {

    }

    public void setId(Long id){ this.id = id; }
    public Long getId(){ return id; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

}
