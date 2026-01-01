package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.Goal;
import com.sipsync.sipsync.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoalService {

    private final GoalRepository goalRepo;
    public GoalService(GoalRepository goalRepo){
        this.goalRepo = goalRepo;
    }

    // set user goal
    public void setGoal(float amount, Long userId) {

        // if goal already exits edit the existing one
        Optional<Goal> result = goalRepo.findByUserId(userId);
        if (result.isPresent()) {
            goalRepo.updateGoal(amount, userId);

        } else {
            Goal goal = new Goal();
            goal.setGoal(amount);
            goal.setUserId(userId);
            goalRepo.save(goal);
        }
    }


    public Float getSetGoal(Long userId) {
        // check if user has set their goal
        Optional<Goal> result = goalRepo.findByUserId(userId);

        if (result.isPresent()) {
            // return if found
            Goal res = result.get();
            Float goal = res.getGoal();

            return goal;
        }
        return 0f;
    }
}



