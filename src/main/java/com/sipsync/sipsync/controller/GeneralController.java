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


    // load homepage
    @GetMapping("/")
    public String MainPage(){
        return "SignUpPage";
    }



    // get total amount drank today
    @ResponseBody
    @GetMapping("/today")
    public TotalsRecord getTodayTotals(){
        return service.totals("DAILY");
    }

    // get total amount drank this week
    @ResponseBody
    @GetMapping("/weekly")
    public TotalsRecord getWeeklyTotals(){
        return service.totals("WEEKLY");
    }


    // get total amount drank this month
    @ResponseBody
    @GetMapping("/monthly")
    public TotalsRecord getMonthlyTotals(){
        return service.totals("MONTHLY");
    }



    // add amount
    @ResponseBody
    @PostMapping("/add")
    public void addLog(@RequestParam int add){
        service.addLog(add);
    }


    // add goal
    @ResponseBody
    @PostMapping("/add/goal")
    public void setGoal(@RequestParam int goal){
        service.setGoal(goal);
    }


    // get set goal
    @ResponseBody
    @GetMapping("/goal")
    public GoalRecord getSetGoal(){
        return service.getSetGoal();
    }



    // edit latest value
    @ResponseBody
    @PostMapping("/add/edit")
    public void editLatestValue(@RequestParam int value){
        service.editLatestValue(value);
    }

}
