package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.service.GoalRecord;
import com.sipsync.sipsync.service.Services;
import com.sipsync.sipsync.service.TotalsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public TotalsRecord getTodayTotals(){
        return service.todayTotal();
    }



    @ResponseBody
    @PostMapping("/add")
    public void addLog(@RequestParam int add){
         service.addLog(add);
    }

    @ResponseBody
    @PostMapping("/add/goal")
    public void setGoal(@RequestParam int goal){
        service.setGoal(goal);
    }

    @ResponseBody
    @GetMapping("/goal")
    public GoalRecord getSetGoal(){
        return service.getSetGoal();
    }








}
