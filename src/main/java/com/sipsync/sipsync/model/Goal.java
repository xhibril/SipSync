package com.sipsync.sipsync.model;

import jakarta.persistence.*;

@Entity
@Table(name = "goal")
public class Goal {
    @Id

    private Long id = 1L;
    Float goal;

    public void setGoal(float goal){this.goal = goal;}
    public Float getGoal(){return goal;}
}
