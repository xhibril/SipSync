package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddUserGoalRepository extends JpaRepository<Goal, Long>{
    Optional<Goal> findByUserId(Long userid);
}
