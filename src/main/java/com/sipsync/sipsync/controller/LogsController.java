package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.service.AuthService;
import com.sipsync.sipsync.service.LogsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LogsController {

    private final AuthService authService;
    private final LogsService logsService;

    public LogsController(AuthService authService, LogsService logsService){
        this.authService = authService;
        this.logsService = logsService;
    }

    @GetMapping("/log/today")
    public ResponseEntity<List> getTodayLogs(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(logsService.todayLogs(userId));
    }


    @PostMapping("/log/add")
    public ResponseEntity<Logs> addLog(@RequestParam int amount, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(logsService.addLog(amount, userId));
    }


    @PostMapping("/log/delete")
    public ResponseEntity<Void> deleteLog(@RequestParam Long logId, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        logsService.deleteLog(userId, logId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/log/update")
    public ResponseEntity<Void> updateLog(@RequestParam int amount, Long id, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        logsService.updateLog(amount, userId, id);
        return ResponseEntity.ok().build();
    }
}
