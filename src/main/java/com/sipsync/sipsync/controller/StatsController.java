package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.AuthService;
import com.sipsync.sipsync.service.StatsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {
    private final AuthService authService;
    private final StatsService statsService;

    public StatsController(AuthService authService, StatsService statsService){
        this.authService = authService;
        this.statsService = statsService;
    }

    // get total amount drank today
    @GetMapping("/stats/daily")
    public ResponseEntity<Float> getTodayTotals(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(statsService.stats("DAILY", userId));
    }


    // get total amount drank this week
    @GetMapping("/stats/weekly")
    public ResponseEntity<Float> getWeeklyTotals(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(statsService.stats("WEEKLY", userId));
    }


    // get total amount drank this month
    @GetMapping("/stats/monthly")
    public ResponseEntity<Float> getMonthlyTotals(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(statsService.stats("MONTHLY", userId));
    }
}
