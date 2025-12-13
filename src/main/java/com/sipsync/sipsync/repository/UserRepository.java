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

    // needed to check if email is already registered
    @Query("SELECT u.email FROM User u WHERE u.email = :email")
    Optional<String> findEmailByEmail(@Param("email") String email);


    Optional<Long> findByUserId(Long userId);

    @Query("SELECT u.streak FROM User u WHERE u.id = :userId")
    Integer findStreakByUserId(Long userId);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findIdByEmail(String email);

    @Query("SELECT u.isVerified FROM User u WHERE u.email = :email")
    Boolean findUserByEmail(String email);

    @Query("SELECT u.lastStreakUpdateDate FROM User u WHERE u.id =:userId")
    String findLastStreakUpdate(Long userId);




    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.streak = :streak, u.lastStreakUpdateDate = :lastStreakUpdateDate WHERE u.id = :userId")
    void updateStreak(@Param("streak") int streak,
                      @Param("lastStreakUpdateDate") String date,
                      @Param("userId") Long userId);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.streak = 0, u.lastStreakUpdateDate = null WHERE u.id = :id")
     void deleteUserStreak(@Param("id") Long id);

}
