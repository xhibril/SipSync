package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @Autowired
    private AuthService authService;

    // home page
    @GetMapping("/home")
    public String MainPage(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "redirect:/login";
        } else {
            return "HomePage";
        }
    }

    // load verification page
    @GetMapping("/verify")
    public String verifyPage(){
        return "VerifyPage";
    }


    // feedback page
    @GetMapping("/feedback")
    public String FeedbackPage(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "redirect:/login";
        } else {
            return "FeedbackPage";
        }
    }

    // load login page
    @GetMapping("/login")
    public String LoginPage(HttpServletRequest req){
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "LoginPage";
        } else {
            return "redirect:/home";
        }
    }

    // load sign up page
    @GetMapping("/signup")
    public String SignUpPage(HttpServletRequest req){
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "SignUpPage";
        } else {
            return "redirect:/home";
        }
    }

}
