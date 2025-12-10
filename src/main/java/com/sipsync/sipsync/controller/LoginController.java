package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired LoginService loginService;

    @GetMapping("/login")
    public String LoginPage(){return "LoginPage";}

    @ResponseBody
    @PostMapping("/api/login")
    public ResponseEntity<String> isUserValid(@RequestBody User user, HttpServletResponse res) {
        try {
            String result = loginService.isUserValid(user.getEmail(), user.getPassword(), res);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
        return ResponseEntity.status(500).body("Server encountered an error.");
        }
    }
}


