package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.Feedback;
import com.sipsync.sipsync.service.AuthService;
import com.sipsync.sipsync.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {

    @Autowired AuthService authService;
    @Autowired FeedbackService feedbackService;

    // post a feedback
    @PostMapping("/api/feedback")
    public ResponseEntity<Void> postFeedback(@RequestBody Feedback feedback, HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        feedbackService.saveFeedback(userId, feedback.getName(), feedback.getEmail(), feedback.getMessage());
        return ResponseEntity.ok().build();
    }
}
