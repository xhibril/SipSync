package com.sipsync.sipsync.service;
import com.sipsync.sipsync.repository.LogsRepository;
import com.sipsync.sipsync.repository.GoalRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired LogsRepository addRepo;
    @Autowired GoalRepository addUserGoalRepo;
    @Autowired UserRepository userRepo;

    // delete account data
    public void resetData(Long userId) {
        addRepo.deleteUserDataLogs(userId);
        addUserGoalRepo.deleteUserDataGoal(userId);
        userRepo.resetUserStreak(userId);
    }
}
