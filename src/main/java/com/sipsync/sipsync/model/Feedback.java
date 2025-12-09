package com.sipsync.sipsync.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String name;
    private String email;
    private String message;


    public Feedback(Long userId, String name, String email, String message){
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.message = message;
    }

    public Feedback() {

    }


    public void setUserId(Long userId){this.userId = userId;}
    public Long getUserId(){return userId;}


    public void setName(String name){this.name = name;}
    public String getName(){return name;}

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

    public void setMessage(String message){ this.message = message;}
    public String getMessage(){return message;}

    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}
}
