package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.AuthService;
import com.sipsync.sipsync.service.GoalService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GoalController {

    @Autowired AuthService authService;
    @Autowired GoalService goalService;


    // add goal
    @PostMapping("/add/goal")
    public ResponseEntity<Void> setGoal(@RequestParam int goal, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        goalService.setGoal(goal, userId);
        return ResponseEntity.ok().build();
    }

    // get user goal
    @GetMapping("/goal")
    public ResponseEntity<Float> getSetGoal(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(goalService.getSetGoal(userId));
    }
}
