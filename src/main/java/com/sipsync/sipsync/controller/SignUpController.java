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

    // load sign up page
    @GetMapping("/signup")
    public String SignUpPage(){return "SignUpPage";}

    // sign up user
    @ResponseBody
    @PostMapping("/api/signup")
    public Boolean addUser(@RequestBody User user) {
        Boolean doesUserExist = singUpService.checkIfUserExists(user.getEmail());

        if(doesUserExist){
            // returning false meaning user already exist and user was not able to be signed up
            return false;
        } else {
            // return true if user was able to be signed up
            singUpService.addUser(user.getEmail(), user.getPassword());
            return true;
        }
    }

}
