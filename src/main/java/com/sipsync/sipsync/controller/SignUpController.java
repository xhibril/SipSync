package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.SingUpService;
import com.sipsync.sipsync.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class SignUpController {

    @Autowired private SingUpService singUpService;
    @Autowired private TokenService tokenService;

    @GetMapping("/signup")
    public String SignUpPage(){return "SignUpPage";}

    // sign up user
    @PostMapping("/api/signup")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            singUpService.addUser(user.getEmail(), user.getPassword());
            return ResponseEntity.ok("Success.");
        } catch(Exception e) {
            return ResponseEntity.status(500).body("Server encountered an error");
        }
    }

    // return verify page
    @GetMapping("/verify")
    public String verifyPage(){
        return "VerifyPage";
    }


    // verify user email
    @ResponseBody
    @GetMapping("/email/check-token")
    public String verifyUser(@RequestParam("token")String token){
        if(singUpService.verifyUser(token)){
            return "HomePage";
        } else{
            return "VerifyPage";
        }
    }



    @ResponseBody
    @PostMapping("/email/send-token")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody User user){

        try {
            String token = tokenService.genTokenAfterSignUp(user.getId(), user.getEmail());
            singUpService.sendVerificationEmail(user.getEmail(), token);
            return ResponseEntity.ok("Success.");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Server encountered an error.");
        }
    }



}
