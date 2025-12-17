package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.AddUserGoalRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired AddLogRepository addRepo;
    @Autowired AddUserGoalRepository addUserGoalRepo;
    @Autowired UserRepository userRepo;

    public void resetData(Long userId){
        addRepo.deleteUserDataLogs(userId);
        addUserGoalRepo.deleteUserDataGoal(userId);
        userRepo.deleteUserStreak(userId);
    }

}
