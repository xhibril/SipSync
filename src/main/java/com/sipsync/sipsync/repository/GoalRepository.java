package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>{
    Optional<Goal> findByUserId(Long userid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Goal u WHERE u.userId = :userId")
    void deleteUserDataGoal(@Param("userId") Long userId );

    @Transactional
    @Modifying
    @Query("UPDATE Goal u SET u.goal = :goal WHERE u.userId = :userId")
    void updateGoal(@Param("goal") Float goal,
                    @Param("userId") Long userId);
}
