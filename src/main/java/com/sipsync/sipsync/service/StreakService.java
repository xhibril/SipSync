package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.List;

@Service
public class StreakService {

    @Autowired AddLogRepository logsRepo;
    @Autowired UserRepository userRepo;

    @Autowired  GoalService goalService;


   // check if more than a day has passed since user last entered and amount
    public int calcStreak(Long userId){

        // check last time user entered a log
        String time = latestTimeRecord(userId);
        if (time == null){
            return 0;
        }

        LocalDate start = LocalDate.parse(time);
        LocalDate end = LocalDate.now();


        Long daysSinceLast = ChronoUnit.DAYS.between(start, end);

        Float goal = goalService.getSetGoal(userId);

        // if it is more than a day reset the streak
        if(daysSinceLast > 1){
            userRepo.updateStreak(0, "Not set" , userId);
            return 0;
        }

        Float amountDrank = 0f;
        // get all the amounts stored in that day to check if it is >= goal
        List<Logs> logs = logsRepo.findByUserIdAndDate(userId, time);

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
        if (log == null) {
            return null;
        }
        return log.getDate();
    }






    public int incrementStreak(Long userId){
        // check if streak has been incremented today
        String lastStreakUpdateDate = userRepo.findLastStreakUpdate(userId);
        String today = LocalDate.now().toString();

        int streak = userRepo.findStreakByUserId(userId);

        if(!(today.equals(lastStreakUpdateDate))){
            streak++;
            userRepo.updateStreak(streak, today , userId);
        }
        return streak;
    }


    public int getStreakStored(Long userId){
        int streak = userRepo.findStreakByUserId(userId);
        return streak;
    }



}
