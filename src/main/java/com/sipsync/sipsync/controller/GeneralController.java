package com.sipsync.sipsync.controller;

import com.sipsync.sipsync.model.Feedback;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.service.*;
import com.sun.net.httpserver.HttpsServer;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class GeneralController {

    @Autowired
    private Services service;
    @Autowired
    private StreakService streakService;
    @Autowired
    private LogsService logsService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private AuthService authService;

    @GetMapping("/home")
    public String MainPage(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "redirect:/login";
        } else {
            return "HomePage";
        }
    }

    @GetMapping("/feedback")
    public String FeedbackPage(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return "redirect:/login";
        } else {
            return "FeedbackPage";
        }
    }


    @ResponseBody
    @GetMapping("/logs/today")
    public ResponseEntity<List> getTodayLogs(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
            return ResponseEntity.ok(logsService.todayLogs(userId));
    }

    @ResponseBody
    @PostMapping("/delete/log")
    public ResponseEntity<Void> deleteLog(@RequestParam Long logId, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            logsService.deleteSingleLog(userId, logId);
            return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/update/amount")
    public ResponseEntity<Void> updateAmount(@RequestParam int amount, Long id, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            logsService.updateAmount(amount, userId, id);
            return ResponseEntity.ok().build();
    }


    // get total amount drank today
    @ResponseBody
    @GetMapping("/today")
    public ResponseEntity<Integer> getTodayTotals(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            return ResponseEntity.ok(service.totals("DAILY", userId));
    }

    // get total amount drank this week
    @ResponseBody
    @GetMapping("/weekly")
    public ResponseEntity<Integer> getWeeklyTotals(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            return ResponseEntity.ok(service.totals("WEEKLY", userId));
    }


    // get total amount drank this month
    @ResponseBody
    @GetMapping("/monthly")
    public ResponseEntity<Integer> getMonthlyTotals(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            return ResponseEntity.ok(service.totals("MOONTLY", userId));
    }


    // add amount
    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<Logs> addLog(@RequestParam int amount, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(service.addLog(amount, userId));
    }


    // add goal
    @ResponseBody
    @PostMapping("/add/goal")
    public ResponseEntity<Void> setGoal(@RequestParam int goal, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        service.setGoal(goal, userId);
        return ResponseEntity.ok().build();
    }


    // get set goal
    @ResponseBody
    @GetMapping("/goal")
    public ResponseEntity<Float> getSetGoal(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(service.getSetGoal(userId));
    }


    // get user streak
    @ResponseBody
    @GetMapping("/check/streak")
    public ResponseEntity<Integer> updateStreak(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(streakService.calcStreak(userId));
    }

    // get already stored streak
    @ResponseBody
    @GetMapping("/get/streak")
    public ResponseEntity<Integer> getStreak(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if(userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(streakService.getStreakStored(userId));
    }


    // increase streak
    @ResponseBody
    @PostMapping("/increment/streak")
    public ResponseEntity<Integer> setStreak(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(streakService.incrementStreak(userId));
    }

    // reset user data
    @ResponseBody
    @PostMapping("/reset/data")
    public ResponseEntity<Void> resetData(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
         service.resetData(userId);
         return ResponseEntity.ok().build();
    }


    // post a feedback
    @PostMapping("/api/feedback")
    public void postFeedback(@RequestBody Feedback feedback, HttpServletRequest req) {
      Long userId = authService.getAuthenticatedUserId(req);
      feedbackService.saveFeedback(userId, feedback.getName(), feedback.getEmail(), feedback.getMessage());
    }


    // find user id by email
    @ResponseBody
    @GetMapping("/users/id")
    public Long findUserIdByEmail(@RequestParam String email) {
        return service.getUserIdByEmail(email);
    }
}

