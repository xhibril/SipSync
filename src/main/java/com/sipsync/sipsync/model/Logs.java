package com.sipsync.sipsync.model;
import jakarta.persistence.*;

@Entity
@Table(name = "waterlogs")
public class Logs {

    // Let the database itself handle ID creation for new rows
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private int amount;
    private String date;
    private String time;


    public void setAmount(int amount){this.amount = amount;}
    public int getAmount(){return amount;}

    public void setDate(String date){this.date = date;}
    public String getDate(){return date;}

    public String getTime(){ return time; }
    public void setTime(String time){  this.time = time; }


    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public Long getUserId(){ return userId; }
    public void setUserId(Long userId){  this.userId = userId; }

}
