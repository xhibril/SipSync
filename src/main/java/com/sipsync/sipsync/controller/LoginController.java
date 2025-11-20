package com.sipsync.sipsync.controller;

import com.sipsync.sipsync.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @ResponseBody
    @GetMapping("/Login")
    public String isUserValid(@RequestParam String email,
                              @RequestParam String password,
                              HttpServletResponse res) {

     return loginService.isUserValid(email, password, res);
    }





}


