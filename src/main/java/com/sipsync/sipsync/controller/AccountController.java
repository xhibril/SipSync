package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.AccountService;
import com.sipsync.sipsync.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired AuthService authService;
    @Autowired AccountService accountService;


    // reset user data
    @PostMapping("/reset/data")
    public ResponseEntity<Void> resetData(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        accountService.resetData(userId);
        return ResponseEntity.ok().build();
    }

}
