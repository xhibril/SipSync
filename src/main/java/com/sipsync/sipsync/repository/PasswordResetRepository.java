package com.sipsync.sipsync.repository;

import com.sipsync.sipsync.model.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.*;

public interface PasswordResetRepository extends JpaRepository<PasswordResetRequest, Long> {

    Optional<PasswordResetRequest> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE PasswordResetRequest u SET u.attemptsRemaining = :attemptsRemaining WHERE u.email =:email")
    void updateAttemptsRemaining(@RequestParam("attemptsRemaining") Integer attemptsRemaining,
                                 @RequestParam("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE PasswordResetRequest u SET u.resetToken = :resetToken, u.resetTokenExpiration =:resetTokenExpiration WHERE u.email =:email")
    void addResetToken(@RequestParam("resetToken") String resetToken,
                       @RequestParam("resetTokenExpiration") Instant resetTokenExpiration);


}
