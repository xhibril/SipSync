package com.sipsync.sipsync.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DbFixController {

    @PersistenceContext
    private EntityManager em;

    @PostMapping("/fix-db")
    @Transactional
    public String fixDb() {
        em.createNativeQuery("""
            ALTER TABLE users
            MODIFY id BIGINT NOT NULL AUTO_INCREMENT
        """).executeUpdate();

        return "done";
    }
}
