package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.dto.auth.PasswordResetRequest;
import com.sipsync.sipsync.dto.auth.PasswordResetResponse;
import com.sipsync.sipsync.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    public PasswordResetController(PasswordResetService passwordResetService){
        this.passwordResetService = passwordResetService;
    }
    // generates verification code and stores it to database
    @PostMapping("/password/reset/code")
    public ResponseEntity<PasswordResetResponse> generateVerificationCode(@RequestBody PasswordResetRequest request) {
      return passwordResetService.requestPasswordReset(request.getEmail());
    }

    // compares verification code user enters with the one stored
    @PostMapping("/password/reset/verify")
        public ResponseEntity<PasswordResetResponse> compareVerificationCode(@RequestBody PasswordResetRequest request){
        return passwordResetService.compareVerificationCode(request.getCode(), request.getEmail());
    }

    // change password
    @PostMapping("/password/reset/change")
    public ResponseEntity<PasswordResetResponse> changePassword(@RequestBody PasswordResetRequest request){
        return passwordResetService.changePassword(request.getEmail(), request.getPassword(), request.getResetToken());
    }
}
