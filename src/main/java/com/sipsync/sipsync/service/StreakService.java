package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.LogsRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;


@Service
public class StreakService {
    private final LogsRepository logsRepo;
    private final UserRepository userRepo;

    public StreakService(LogsRepository logsRepo,
                         UserRepository userRepo)
    {
        this.logsRepo = logsRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public int evaluateStreak(Long userId){
        LocalDate lastStreakUpdateDate = userRepo.findLastStreakUpdate(userId);
        if(lastStreakUpdateDate == null){
            return 0;
        }

        long daysGap = ChronoUnit.DAYS.between(lastStreakUpdateDate, LocalDate.now());
        // streak broken if day missed
        if (daysGap > 1) {
            userRepo.resetUserStreak(userId);
            return 0;
        }

        return getStreakStored(userId);
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
