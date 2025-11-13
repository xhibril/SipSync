package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetGoalRepository extends JpaRepository<Goal, Long>{
}
