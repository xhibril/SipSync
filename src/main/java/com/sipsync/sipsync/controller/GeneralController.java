package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class GeneralController {

    @Autowired
    private AddLogRepository repo;

    @Autowired
    private Services service;

    @GetMapping("/")
    public String MainPage(){
        return "HomePage";
    }

    @GetMapping("/add")
    public String addPage(){
        service.todayTotal();
        return "HomePage";
    }

    @PostMapping("/add")
    public String addLog(@RequestParam int add){
        service.addLog(add);
        return "redirect:/";
    }









}
