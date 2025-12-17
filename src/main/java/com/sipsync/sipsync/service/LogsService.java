package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LogsService {

    @Autowired
    AddLogRepository logsRepo;

    @Autowired AddLogRepository addRepo;

    public List todayLogs(Long userId){
        LocalDate today = LocalDate.now();
        List<Logs> todayLogs= logsRepo.findByUserIdAndDate(userId, today.toString());

        return todayLogs;
    }


    // add amount
    public Logs addLog(int amount, Long userId) {

        Logs log = new Logs();
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("h:mm a");
        String timeString = time.format(formater);

        log.setUserId(userId);
        log.setAmount(amount);
        log.setDate(String.valueOf(today));
        log.setTime(timeString);
        Logs saved = addRepo.save(log);

        return saved;
    }

    public void updateLog(int amount, Long userId, Long id){
        logsRepo.updateLog(amount, userId, id);
    }

    public void deleteSingleLog(Long userId, Long logId){
        logsRepo.deleteSingleLog(userId, logId);
    }



}
