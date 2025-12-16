package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.LoginRequest;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.LoginService;
import com.sipsync.sipsync.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired LoginService loginService;
    @Autowired
    AuthService verificationService;

    @GetMapping("/login")
    public String LoginPage(){return "LoginPage";}

    // check if user credentials are correct
    @ResponseBody
    @PostMapping("/api/login")
    public Boolean areCredentialsValid(@RequestBody LoginRequest request, HttpServletResponse res) {
            return loginService.isUserValid(request.getEmail(), request.getPassword(), request.getRememberMe(), res);
    }


}


