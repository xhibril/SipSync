package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.dto.auth.PasswordResetRequest;
import com.sipsync.sipsync.dto.auth.PasswordResetErrorResponse;
import com.sipsync.sipsync.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordResetController {
    @Autowired PasswordResetService passwordResetService;

    // generates verification code and stores it to database
    @PostMapping("/password-reset")
    public ResponseEntity<PasswordResetErrorResponse> generateVerificationCode(@RequestBody PasswordResetRequest request) {
      return passwordResetService.generateVerificationCode(request.getEmail());
    }

    // compares verification code user enters with the one stored
    @PostMapping("/password-reset/verify")
        public ResponseEntity<PasswordResetErrorResponse> compareVerificationCode(@RequestBody PasswordResetRequest request){
        return passwordResetService.compareVerificationCode(request.getCode(), request.getEmail());
    }

    // change password
    @PostMapping("/password-reset/change")
    public ResponseEntity<PasswordResetErrorResponse> changePassword(@RequestBody PasswordResetRequest request){
        return passwordResetService.changePassword(request.getEmail(), request.getCode(), request.getPassword());
    }

}
