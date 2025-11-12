package com.sipsync.sipsync.model;
import jakarta.persistence.*;

@Entity
@Table(name = "waterlogs")

public class Edit {

    @Id

    private Long id;
    private int amount;


    public int getAmount(){ return amount; }
    public Long getId(){ return id; }

    public void setAmount(int amount){ this.amount = amount; }
    public void setId(Long id){ this.id = id; }

}
