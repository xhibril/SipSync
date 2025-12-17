package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {
    @Autowired private AdminService adminService;

    // find user id by email
    @GetMapping("/users/id")
    public Long findUserIdByEmail(@RequestParam String email) {
        return adminService.getUserIdByEmail(email);
    }
}

