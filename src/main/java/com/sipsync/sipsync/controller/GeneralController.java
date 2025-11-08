package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.service.Services;
import com.sipsync.sipsync.service.Totals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class GeneralController {

    @Autowired private AddLogRepository repo;
    @Autowired private Services service;

    @GetMapping("/")
    // show amount of water drank today in homepage
    public String MainPage(){
        return "HomePage";
    }




    @ResponseBody
    @GetMapping("/today")
    public Totals getTodayTotals(){
        return service.todayTotal();
    }




    @PostMapping("/add")
    public void addLog(@RequestParam int add){
         service.addLog(add);
    }









}
