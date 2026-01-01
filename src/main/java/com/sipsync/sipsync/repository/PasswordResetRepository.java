package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.*;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE PasswordReset u SET u.attemptsRemaining = :attemptsRemaining WHERE u.email =:email")
    void updateAttemptsRemaining(@Param("attemptsRemaining") Integer attemptsRemaining,
                                 @Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE PasswordReset u SET u.resetToken = :resetToken, u.resetTokenExpiration =:resetTokenExpiration WHERE u.email =:email")
    void addResetToken(@Param("resetToken") String resetToken,
                       @Param("resetTokenExpiration") Instant resetTokenExpiration,
                       @Param("email") String email);


    @Modifying
    @Query("UPDATE PasswordReset u SET u.code = null WHERE u.email =:email")
    void clearVerificationCode(@Param("email") String email);


    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
