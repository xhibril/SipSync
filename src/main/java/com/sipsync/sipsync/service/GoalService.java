package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.EditGoal;
import com.sipsync.sipsync.model.Goal;
import com.sipsync.sipsync.repository.AddUserGoalRepository;
import com.sipsync.sipsync.repository.EditUserGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoalService {

    @Autowired
    AddUserGoalRepository addUserGoalRepo;

    @Autowired
    EditUserGoalRepository editUserGoalRepo;


    public void setGoal(int amount, Long userId) {

        // if goal already exits edit the existing one
        Optional<Goal> result = addUserGoalRepo.findByUserId(userId);

        if (result.isPresent()) {

            EditGoal editGoal = new EditGoal();
            Goal goal = new Goal();

            goal = result.get();
            Long id = goal.getId();

            editGoal.setGoal(amount);
            editGoal.setId(id);
            editUserGoalRepo.save(editGoal);
        } else {

            Goal goal = new Goal();
            goal.setGoal(amount);
            goal.setUserId(userId);
            addUserGoalRepo.save(goal);

        }
    }



    public Float getSetGoal(Long userId) {
        Optional<Goal> result = addUserGoalRepo.findByUserId(userId);

        // check if sum is inside the wrapper optional
        if (result.isPresent()) {
            // return if found
            Goal res = result.get();
            Float goal = res.getGoal();

            return goal;
        }
        return 0f;
    }

}
