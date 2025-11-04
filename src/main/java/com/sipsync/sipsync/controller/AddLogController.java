package com.sipsync.sipsync.controller;

import com.sipsync.sipsync.model.AddLog;
import com.sipsync.sipsync.repository.AddLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddLogController {

    @Autowired
    private AddLogRepository repo;


    @PostMapping("/add/db")
    public AddLog addLog(){
        AddLog log = new AddLog();
        log.setAmount(10);
        log.setTime("12:00");

        return repo.save(log);
    }
}
