package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.config.RateLimiter;
import com.sipsync.sipsync.service.AccountService;
import com.sipsync.sipsync.service.AuthService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired AuthService authService;
    @Autowired AccountService accountService;
@Autowired
    RateLimiter rateLimiter;

    // reset user data
    @PostMapping("/data/reset")
    public ResponseEntity<Void> resetData(HttpServletRequest req) {
        Long userId = authService.getAuthenticatedUserId(req);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        accountService.resetData(userId);
        return ResponseEntity.ok().build();
    }

    @RestController
    public class DevController {

        @PersistenceContext
        private EntityManager em;

        @GetMapping("/__drop_users")
        public String dropUsers() {
            em.createNativeQuery("DROP TABLE users").executeUpdate();
            return "users table dropped";
        }
    }


}
