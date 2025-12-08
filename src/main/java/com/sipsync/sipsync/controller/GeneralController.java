package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class GeneralController {

    @Autowired private Services service;
    @Autowired private Cookies cookiesService;
    @Autowired private TokenService tokenService;
    @Autowired private StreakService streakService;
    @Autowired private LogsService logsService;


    @GetMapping({"/", "/login"})
    public String LoginPage(){return "LoginPage";}

    @GetMapping({"/Signup", "/signup"})
    public String SignUpPage(){return "SignUpPage";}

    @GetMapping({"/Home", "/home"})
    public String MainPage(){
        return "HomePage";
    }

    @GetMapping({"/Feedback", "/feedback"})
    public String FeedbackPage(){ return "FeedbackPage";}


    @ResponseBody
    @GetMapping("/logs/today")
    public List getTodayLogs(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return logsService.todayLogs(userId);
    }

    @ResponseBody
    @PostMapping("/delete/log")
    public void deleteLog(@RequestParam Long logId, HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        logsService.deleteSingleLog(userId, logId);
    }

    @ResponseBody
    @PostMapping("/update/amount")
    public void updateAmount(@RequestParam int amount, Long id, HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        logsService.updateAmount(amount, userId, id);
    }


    // get total amount drank today
    @ResponseBody
    @GetMapping("/today")
    public Integer getTodayTotals(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return service.totals("DAILY", userId);
    }

    // get total amount drank this week
    @ResponseBody
    @GetMapping("/weekly")
    public Integer getWeeklyTotals(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return service.totals("WEEKLY", userId);
    }


    // get total amount drank this month
    @ResponseBody
    @GetMapping("/monthly")
    public Integer getMonthlyTotals(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return service.totals("MONTHLY", userId);
    }



    // add amount
    @ResponseBody
    @PostMapping("/add")
    public Logs addLog(@RequestParam int add, HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return service.addLog(add, userId);
    }


    // add goal
    @ResponseBody
    @PostMapping("/add/goal")
    public void setGoal(@RequestParam int goal, HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        service.setGoal(goal, userId);
    }


    // get set goal
    @ResponseBody
    @GetMapping("/goal")
    public Float getSetGoal(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return service.getSetGoal(userId);
    }



    // get user streak
@ResponseBody
@GetMapping("/check/streak")
public int updateStreak(HttpServletRequest req){
    Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
    return streakService.calcStreak(userId);
    }


    @ResponseBody
    @GetMapping("/get/streak")
    public int getStreak(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return streakService.getStreakStored(userId);
    }



    // set streak
    @ResponseBody
    @PostMapping("/increment/streak")
    public int setStreak(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        return streakService.incrementStreak(userId);
    }

    @ResponseBody
    @PostMapping("/reset/data")
    public void resetData(HttpServletRequest req){
        Long userId = tokenService.extractId(cookiesService.getTokenByCookie(req));
        service.resetData(userId);
    }


}

