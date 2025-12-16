package com.sipsync.sipsync.controller;

import com.sipsync.sipsync.model.PasswordResetDTO;
import com.sipsync.sipsync.model.PasswordResetErrorResponse;
import com.sipsync.sipsync.model.PasswordResetRequest;
import com.sipsync.sipsync.service.PasswordResetService;
import io.jsonwebtoken.security.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PasswordResetController {

    @Autowired PasswordResetService passwordResetService;

    // generates verification code and stores it to database
    @ResponseBody
    @PostMapping("/password-reset")
    public ResponseEntity<PasswordResetErrorResponse> generateVerificationCode(@RequestBody PasswordResetDTO request) {
      return passwordResetService.generateVerificationCode(request.getEmail());
    }


    // compares verification code user enters with the one stored
    @ResponseBody
    @PostMapping("/password-reset/verify")
        public ResponseEntity<PasswordResetErrorResponse> compareVerificationCode(@RequestBody PasswordResetDTO request){
        return passwordResetService.compareVerificationCode(request.getCode(), request.getEmail());
    }


    @ResponseBody
    @PostMapping("/password-reset/change")
    public ResponseEntity<PasswordResetErrorResponse> changePassword(@RequestBody PasswordResetDTO request){
        return passwordResetService.changePassword(request.getEmail(), request.getCode(), request.getPassword());
    }

}
