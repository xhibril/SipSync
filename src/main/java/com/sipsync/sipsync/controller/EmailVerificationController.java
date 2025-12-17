package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.TokenService;
import com.sipsync.sipsync.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailVerificationController {

    @Autowired private TokenService tokenService;
    @Autowired private AuthService authService;

    // send verification link to user
    @PostMapping("/email/send-token")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody User user){

        try {
            String token = tokenService.genTokenAfterSignUp(user.getId(), user.getEmail());
            authService.sendVerificationEmail(user.getEmail(), token);
            return ResponseEntity.ok("Success.");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Server encountered an error.");
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
