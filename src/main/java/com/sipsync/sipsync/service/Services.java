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

    
    public todayTotal(Model model){
        List<Logs> totals = repo.findAll();
        int sum = 0;
        for (Logs total : totals){
            model.addAttribute("amount", total.getAmount());
            model.addAttribute("date", total.getTime());
        }


    }







}



