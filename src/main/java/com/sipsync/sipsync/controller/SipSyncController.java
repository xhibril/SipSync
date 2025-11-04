package com.sipsync.sipsync.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class SipSyncController {

    ArrayList<String> logsArr = new ArrayList<>();
    @GetMapping("/")
    public String MainPage(){
        return "HomePage";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addPage(@RequestParam String addLog){
        logsArr.add(addLog);
        return "Added";
    }


    @GetMapping("/totals")
    public String displayTotals(Model model){
        if (logsArr.isEmpty()) {
            model.addAttribute("message", "No logs added yet.");
        } else {
            model.addAttribute("message", "You added " + logsArr.get(0));
        }
        return "TotalsPage";
    }




}
