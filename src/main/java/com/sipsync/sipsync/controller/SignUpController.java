package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.SingUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class SignUpController {

    @Autowired
    private SingUpService service;


    @ResponseBody
    @PostMapping("/Signup")
    public User addUser(@RequestParam String email,  @RequestParam String password) {
     User user =  service.addUser(email, password);
     return user;
    }

    @ResponseBody
    @PostMapping("/SendVerificationEmail")
    public void sendVerificationEmail(@RequestParam String email,  @RequestParam String token){
        service.sendVerificationEmail(email, token);
    }

    @GetMapping("/Home")
    public String ri(){
        return "HomePage";
    }




    @ResponseBody
    @GetMapping("/verify")
    public void verifyEmail(@RequestParam("token")String token){
     service.verifyUser(token);
    }










}
