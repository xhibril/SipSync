package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LogsService {

    @Autowired
    AddLogRepository logsRepo;

    public List todayLogs(Long userId){
        LocalDate today = LocalDate.now();
        List<Logs> todayLogs= logsRepo.findByUserIdAndTime(userId, today.toString());

        return todayLogs;
    }


    public void updateAmount(int amount, Long userId){
        logsRepo.updateAmount(amount, userId);
    }



}
