package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Goal;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.model.Edit;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.EditLogRepository;
import com.sipsync.sipsync.repository.SetGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.rmi.server.LogStream;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class Services {

    @Autowired private AddLogRepository addRepo;
    @Autowired private SetGoalRepository goalRepo;
    @Autowired private EditLogRepository editRepo;


    public void addLog(int add){

        Logs log = new Logs();
        LocalDate today = LocalDate.now();
        log.setAmount(add);
        log.setTime(String.valueOf(today));
        addRepo.save(log);
    }

    
    public TotalsRecord totals(final String type) {
        List<Logs> savedAmounts = addRepo.findAll();
        LocalDate today = LocalDate.now();
        int sum = 0;

        switch (type) {
            case "DAILY":
                for (Logs saved : savedAmounts) {

                    if (String.valueOf(today).equals(saved.getTime())) {
                        sum += saved.getAmount();
                    }
                }
                return new TotalsRecord(sum, String.valueOf(today));

            case "WEEKLY":

                LocalDate sevenDaysAgo = today.minusDays(-7);

                for (Logs saved : savedAmounts) {

                    // convert string to local date obj
                    LocalDate logDate = LocalDate.parse(saved.getTime());

                    if (!(sevenDaysAgo.isBefore(logDate) && !(today.isAfter(logDate)))) {
                        sum += saved.getAmount();
                    }
                }
                return new TotalsRecord(sum, "Weekly");


            case "MONTHLY":

                LocalDate thirtyDaysAgo = today.minusDays(-30);

                for (Logs saved : savedAmounts) {

                    // convert string to local date obj
                    LocalDate logDate = LocalDate.parse(saved.getTime());

                    if (!(thirtyDaysAgo.isBefore(logDate) && !(today.isAfter(logDate)))) {
                        sum += saved.getAmount();
                    }
                }
                return new TotalsRecord(sum, "Monthly");
        }
            return null;
        }









    public void editLatestValue(int value){

        Logs last = addRepo.findTopByOrderByIdDesc();
        Long id = last.getId();

        Edit edit = new Edit();
        edit.setAmount(value);
        edit.setId(id);

        editRepo.save(edit);
    }



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



