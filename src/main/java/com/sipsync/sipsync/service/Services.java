package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Goal;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.SetGoalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class Services {

    @Autowired private AddLogRepository addRepo;
    @Autowired private SetGoalRepository goalRepo;

    @Transactional
    public void addLog(int add){

        Logs log = new Logs();
        LocalDate today = LocalDate.now();
        log.setAmount(add);
        log.setTime(String.valueOf(today));
        addRepo.save(log);
    }

    
    public TotalsRecord todayTotal(){
        List<Logs> savedAmounts = addRepo.findAll();
        LocalDate today = LocalDate.now();
        int sum = 0;
        for (Logs saved : savedAmounts){

            if(String.valueOf(today).equals(saved.getTime())){
                sum += saved.getAmount();
            }

        }
        return new TotalsRecord(sum, String.valueOf(today));
    }




@Transactional
    public void setGoal(float amount){
        Goal goal = new Goal();
        goal.setGoal(amount);
        goalRepo.save(goal);
    }


    public GoalRecord getSetGoal() {
        Optional<Goal> result = goalRepo.findById(1L);

        // check if sum is inside the wrapper optional
        if (result.isPresent()) {
            // return if found
            Goal goal = result.get();
            return new GoalRecord(goal.getGoal());
        } else {
            return null;
        }
    }










}



