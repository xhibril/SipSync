package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.SingUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.Socket;
@Controller
public class SignUpController {

    @Autowired
    private SingUpService service;


    @ResponseBody
    @PostMapping("/Signup")
    public void addUser(@RequestParam String email, String password){
        service.addUser(email, password);
    }



    @ResponseBody
    @GetMapping("/verify")
    public void verifyEmail(@RequestParam("token")String token){
     service.verifyUser(token);
    }










}
