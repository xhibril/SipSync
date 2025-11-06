package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.time.LocalDate;

@Service
public class Services {

    @Autowired
    private AddLogRepository repo;

    public void addLog(int add){

        Logs log = new Logs();
        LocalDate today = LocalDate.now();
        log.setAmount(add);
        log.setTime(String.valueOf(today));
        repo.save(log);
    }

    
    public Totals todayTotal(){
        List<Logs> savedAmounts = repo.findAll();
        LocalDate today = LocalDate.now();
        int sum = 0;
        for (Logs saved : savedAmounts){

            if(String.valueOf(today).equals(saved.getTime())){
                sum += saved.getAmount();
            }

        }
        return new Totals(sum, String.valueOf(today));
    }










}



