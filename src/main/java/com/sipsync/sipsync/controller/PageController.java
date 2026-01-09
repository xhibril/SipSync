package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final AuthService authService;
    public PageController(AuthService authService){
        this.authService = authService;
    }

    // home page
    @GetMapping({"/", "/home"})
    public String MainPage(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "redirect:/login";
        }

        Boolean isVerified = authService.checkVerificationStatusById(userId);
        if(!isVerified){
            return "redirect:/verify";
        }  else{
            return "Dashboard";
        }
    }

    // load verification page
    @GetMapping("/verify")
    public String verifyPage(){
        return "Verification";
    }

    @GetMapping("/calculator")
        public String calculatorPage(){
            return "Calculator";
    }


    // feedback page
    @GetMapping("/feedback")
    public String FeedbackPage(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "redirect:/login";
        }

        Boolean isVerified = authService.checkVerificationStatusById(userId);
        if(!isVerified){
            return "redirect:/verify";
        }  else{
            return "Feedback";
        }
    }

    // load login page
    @GetMapping("/login")
    public String LoginPage(HttpServletRequest req){
            return "Login";
    }

    // load sign up page
    @GetMapping("/signup")
    public String SignUpPage(HttpServletRequest req){
       return "Signup";
    }

}
