package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.AuthService;
import com.sipsync.sipsync.service.StreakService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreakController {
    private final AuthService authService;
    private final StreakService streakService;

    public StreakController(AuthService authService, StreakService streakService){
        this.authService = authService;
        this.streakService = streakService;
    }

    // get user streak
    @GetMapping("/streak/evaluate")
    public ResponseEntity<Integer> updateStreak(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(streakService.evaluateStreak(userId));
    }


    // increase streak
    @PostMapping("/streak/increment")
    public ResponseEntity<Integer> setStreak(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(streakService.incrementStreakIfNotUpdatedToday(userId));
    }
}
