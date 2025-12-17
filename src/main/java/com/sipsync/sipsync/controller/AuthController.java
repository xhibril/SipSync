package com.sipsync.sipsync.controller;

import com.sipsync.sipsync.dto.auth.LoginRequest;
import com.sipsync.sipsync.dto.auth.SignUpRequest;
import com.sipsync.sipsync.service.AuthService;
import com.sipsync.sipsync.service.LoginService;
import com.sipsync.sipsync.service.SingUpService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class AuthController {

    @Autowired private SingUpService singUpService;
    @Autowired LoginService loginService;
    @Autowired AuthService authService;

    // check if user credentials are correct
    @PostMapping("/api/login")
    public Boolean areCredentialsValid(@RequestBody LoginRequest request, HttpServletResponse res) {
        return loginService.isUserValid(request.getEmail(), request.getPassword(), request.getRememberMe(), res);
    }

    // check if user is verified after logging in
    @GetMapping("/api/verification-status")
    public Boolean isUserVerified(@RequestParam String email) {
        return authService.isUserVerified(email);
    }

    // sign up user
    @PostMapping("/api/signup")
    public Boolean addUser(@RequestBody SignUpRequest request) {
        return singUpService.addUser(request.getEmail(), request.getPassword());
    }
}
