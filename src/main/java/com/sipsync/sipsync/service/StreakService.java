package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Goal;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.List;

@Service
public class StreakService {

    @Autowired
    AddLogRepository logsRepo;

    @Autowired
    UserRepository userRepo;





    @Autowired
    Services services;


// check if more than a day has passed since user last entered and amount
    public int calcStreak(Long userId){
        String time = latestTimeRecord(userId);


        LocalDate start = LocalDate.parse(time);
        LocalDate end = LocalDate.now();
        Long daysSinceLast = ChronoUnit.DAYS.between(start, end);

        Float goal = services.getSetGoal(userId);


        if(daysSinceLast > 1){
            return 0;
        }

        Float amountDrank = 0f;
        // get all the amounts stored in that day to check if it is >= goal
        List<Logs> logs = logsRepo.findByUserIdAndTime(userId, time);

        for(Logs saved : logs) {
            amountDrank += saved.getAmount();
        }

        if(amountDrank >= goal){
            return getStreakStored(userId);
        } else {
            return 0;
        }
    }


    // get the latest date the user entered a value
    public String latestTimeRecord(Long userId){
        Logs log = logsRepo.findTopByUserIdOrderByIdDesc(userId);
        return log.getTime();
    }


    public int getStreakStored(Long userId){
        User user = new User();
        return userRepo.findStreakByUserId(userId);
    }



}
