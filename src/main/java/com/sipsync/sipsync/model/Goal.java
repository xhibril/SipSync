package com.sipsync.sipsync.model;

import jakarta.persistence.*;

@Entity
@Table(name = "goal")
public class Goal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private Float goal;

    public void setGoal(float goal){this.goal = goal;}
    public Float getGoal(){return goal;}

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
}
