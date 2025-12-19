package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.LogsRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.hibernate.boot.jaxb.internal.stax.LocalSchemaLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.List;

@Service
public class StreakService {
    @Autowired LogsRepository logsRepo;
    @Autowired UserRepository userRepo;
    @Autowired GoalService goalService;


   // check if more than a day has passed since user last entered and amount
    public int evaluateStreak(Long userId){

        // check last time user entered a log
        LocalDate start = getLastLogDate(userId);
        if (start == null){
            return 0;
        }

        LocalDate end = LocalDate.now();
        Long daysSinceLast = ChronoUnit.DAYS.between(start, end);

        // if it is more than a day reset the streak
        if(daysSinceLast > 1){
            // null is last update streak date
            userRepo.updateStreak(0, null , userId);
            return 0;
        }

        // get all the amounts stored in that day to check if it is >= goal
        if(didMeetGoalOnDate(userId, start)){
            return getStreakStored(userId);
        } else {
            return 0;
        }
    }


    // check if goal was reached
    private boolean didMeetGoalOnDate(Long userId, LocalDate time){
        Float amountDrank = 0f;
        Float goal = goalService.getSetGoal(userId);
        List<Logs> logs = logsRepo.findByDateAndUserId(time, userId);

        for(Logs saved : logs) {
            amountDrank += saved.getAmount();
        }

        if(amountDrank >= goal){
            return true;
        } else {
            return false;
        }
    }


    // get the latest date the user entered a value
    private LocalDate getLastLogDate(Long userId){
        Logs log = logsRepo.findTopByUserIdOrderByIdDesc(userId);
        if (log == null) {
            return null;
        }
        return log.getDate();
    }


    public int incrementStreakIfNotUpdatedToday(Long userId){
        // check if streak has been incremented today
        LocalDate lastStreakUpdateDate = userRepo.findLastStreakUpdate(userId);
        LocalDate today = LocalDate.now();

        int streak = userRepo.findStreakByUserId(userId);

        if(!(today.equals(lastStreakUpdateDate))){
            streak++;
            userRepo.updateStreak(streak, today, userId);
        }
        return streak;
    }


    public int getStreakStored(Long userId){
        return userRepo.findStreakByUserId(userId);
    }
}
