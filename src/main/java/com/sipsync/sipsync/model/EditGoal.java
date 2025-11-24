package com.sipsync.sipsync.model;
import jakarta.persistence.*;

@Entity
@Table(name = "goal")
public class EditGoal {


    @Id
    private Long id;

    private Float goal;

    public void setGoal(float goal){this.goal = goal;}
    public Float getGoal(){return goal;}


    public void setId(Long id) {this.id = id;}
    public Long getId(){ return id; }


}
