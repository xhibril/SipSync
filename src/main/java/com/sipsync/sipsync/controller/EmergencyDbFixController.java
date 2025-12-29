package com.sipsync.sipsync.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmergencyDbFixController {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @GetMapping("/__drop_users")
    public String dropUsersTable() {
        em.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
        return "users table dropped";
    }
}
