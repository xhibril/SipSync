package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmailVerificationController {

    private final AuthService authService;
    public EmailVerificationController(AuthService authService){
        this.authService = authService;
    }

    // send verification link to user
    @PostMapping("/email/send-token")
    @ResponseBody
    public void sendVerificationEmail(@RequestBody User user){
          if(user.getId() == null){
              Long userId = authService.getUserIdByEmail(user.getEmail());
              authService.generateAuthTokenAfterSignup(userId, user.getEmail());
          } else{
              authService.generateAuthTokenAfterSignup(user.getId(), user.getEmail());
          }
    }

    // verify user email
    @GetMapping("/email/check-token")
    public String verifyUser(@RequestParam("token")String token){
        Boolean verified = authService.verifyUser(token);
        if(verified){
            return "redirect:/login?verified=true";
        } else {
            return "redirect:/login?verified=false";
        }
    }
}
