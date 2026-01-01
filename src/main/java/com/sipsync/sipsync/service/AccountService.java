package com.sipsync.sipsync.service;
import com.sipsync.sipsync.repository.LogsRepository;
import com.sipsync.sipsync.repository.GoalRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final LogsRepository logsRepo;
    private final GoalRepository goalRepo;
    private final UserRepository userRepo;

    public AccountService(LogsRepository logsRepo,
                          GoalRepository goalRepo,
                          UserRepository userRepo)
    {
        this.logsRepo = logsRepo;
        this.goalRepo = goalRepo;
        this.userRepo = userRepo;
    }


    // delete account data
    public void resetData(Long userId) {
        logsRepo.deleteUserDataLogs(userId);
        goalRepo.deleteUserDataGoal(userId);
        userRepo.resetUserStreak(userId);
    }
}
