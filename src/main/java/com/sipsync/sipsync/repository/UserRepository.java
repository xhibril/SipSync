package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.isVerified FROM User u WHERE u.id = :userId")
    Boolean findIsVerifiedById(@Param("userId")Long userId);

    @Query("SELECT u.isVerified FROM User u WHERE u.email = :email")
    Boolean findIsVerifiedByEmail(@Param("email")String email);

    // needed to check if email is already registered
    @Query("SELECT u.email FROM User u WHERE u.email = :email")
    Optional<String> findEmailByEmail(@Param("email") String email);

    boolean existsById(Long userId);

    @Query("SELECT u.streak FROM User u WHERE u.id = :userId")
    Integer findStreakByUserId(Long userId);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findIdByEmail(String email);

    @Query("SELECT u.isVerified FROM User u WHERE u.email = :email")
    Boolean findUserByEmail(String email);

    @Query("SELECT u.lastStreakUpdateDate FROM User u WHERE u.id =:userId")
    LocalDate findLastStreakUpdate(Long userId);




    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.streak = :streak, u.lastStreakUpdateDate = :lastStreakUpdateDate WHERE u.id = :userId")
    void updateStreak(@Param("streak") int streak,
                      @Param("lastStreakUpdateDate") LocalDate date,
                      @Param("userId") Long userId);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.streak = 0, u.lastStreakUpdateDate = null WHERE u.id = :id")
     void deleteUserStreak(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void changePassword(@Param("password") String password,
                        @Param("email") String email);


    boolean existsByEmail(String email);
}
