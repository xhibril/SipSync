package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.dto.auth.LoginRequest;
import com.sipsync.sipsync.dto.auth.SignUpRequest;
import com.sipsync.sipsync.service.AuthService;
import com.sipsync.sipsync.service.LoginService;
import com.sipsync.sipsync.service.SignUpService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final SignUpService signUpService;
    private final LoginService loginService;
    private final AuthService authService;


    public AuthController(SignUpService signUpService,
                          LoginService loginService,
                          AuthService authService){
        this.signUpService = signUpService;
        this.loginService = loginService;
        this.authService = authService;
    }


    @PostMapping("/api/login")
    public Boolean areCredentialsValid(@RequestBody LoginRequest request, HttpServletResponse res) {
        return loginService.isUserValid(request.getEmail(), request.getPassword(), request.getRememberMe(), res);
    }

    // check if user is verified after logging in
    @PostMapping("/api/verification-status")
    public Boolean isUserVerified(@RequestBody SignUpRequest request) {
        return authService.checkVerificationStatusByEmail(request.getEmail());
    }

    // sign up user
    @PostMapping("/api/signup")
    public Boolean addUser(@RequestBody SignUpRequest request) {
        return signUpService.addUser(request.getEmail(), request.getPassword());
    }


    @PostMapping("/api/logout")
    public Boolean logout(HttpServletResponse res){
        return loginService.logout(res);
    }
}
