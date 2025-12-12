package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.LoginService;
import com.sipsync.sipsync.service.VerificationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired LoginService loginService;
    @Autowired VerificationService verificationService;

    @GetMapping("/login")
    public String LoginPage(){return "LoginPage";}

    // check if user credentials are correct
    @ResponseBody
    @PostMapping("/api/login")
    public Boolean areCredentialsValid(@RequestBody User user, HttpServletResponse res) {
            return loginService.isUserValid(user.getEmail(), user.getPassword(), res);
    }


}


