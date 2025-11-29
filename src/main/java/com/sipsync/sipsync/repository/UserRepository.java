package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.streak FROM User u WHERE u.id = :userId")
    int findStreakByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.streak = :streak WHERE u.id = :userId")
    void updateStreak(@Param("streak") int streak,
                      @Param("userId") Long userId);
}
