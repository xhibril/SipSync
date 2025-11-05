package com.sipsync.sipsync.model;
import jakarta.persistence.*;

@Entity
@Table(name = "waterlogs")
public class AddLog {

    //   “Let the database itself handle ID creation for new rows — I don’t want to generate IDs manually in Java.”
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    int amount;
    String time;



    public void setAmount(int amount){this.amount = amount;}
    public int getAmount(){return amount;}

    public void setTime(String time){this.time = time;}
    public String getTime(){return time;}
}
