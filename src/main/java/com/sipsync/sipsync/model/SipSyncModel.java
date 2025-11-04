package com.sipsync.sipsync.model;

import org.springframework.web.bind.annotation.ModelAttribute;

public class SipSyncModel {
    int id;
    int amount;
    String time;

    public SipSyncModel(int id, int amount, String time){

        this.id = id;
        this.amount = amount;
        this.time = time;
    }
}
