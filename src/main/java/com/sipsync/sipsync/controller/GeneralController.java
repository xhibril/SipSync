package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.service.Cookies;
import com.sipsync.sipsync.service.GoalRecord;
import com.sipsync.sipsync.service.Services;
import com.sipsync.sipsync.service.TotalsRecord;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GeneralController {

    @Autowired private AddLogRepository repo;
    @Autowired private Services service;
    @Autowired private Cookies cookiesService;



    @GetMapping("/")
    public String LoginPage(){
        return "LoginPage";
    }


    @GetMapping("/Signup")
    public String SignUpPage(){
        return "SignUpPage";
    }


    @GetMapping("/Home")
    public String MainPage(){
        return "HomePage";
    }









    // get total amount drank today
    @ResponseBody
    @GetMapping("/today")
    public TotalsRecord getTodayTotals(HttpServletRequest req){
        return service.totals("DAILY", req);
    }

    // get total amount drank this week
    @ResponseBody
    @GetMapping("/weekly")
    public TotalsRecord getWeeklyTotals(HttpServletRequest req){
        return service.totals("WEEKLY", req);
    }


    // get total amount drank this month
    @ResponseBody
    @GetMapping("/monthly")
    public TotalsRecord getMonthlyTotals(HttpServletRequest req){
        return service.totals("MONTHLY", req);
    }



    // add amount
    @ResponseBody
    @PostMapping("/add")
    public void addLog(@RequestParam int add, HttpServletRequest request){
        service.addLog(add, request);
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
