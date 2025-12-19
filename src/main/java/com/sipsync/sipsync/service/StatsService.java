package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class StatsService {
    @Autowired LogsRepository logsRepo;

    private enum Period {
        WEEKLY,
        MONTHLY
    }

    public Float stats(String type, Long userId) {
        switch (type) {
            case "DAILY": return todayTotal(userId);
            case "WEEKLY": return average(Period.WEEKLY, userId);
            case "MONTHLY": return average(Period.MONTHLY, userId);
        }
        return 0f;
    }


    // today totals
    private Float todayTotal(Long userId) {
        LocalDate today = LocalDate.now();

        List<Logs> todayLogs = logsRepo.findByDateAndUserId(today, userId);
        float sum = 0f;

        for (Logs saved : todayLogs) {
            sum += saved.getAmount();
        }
        return sum;
    }


    // average water drank daily for weekly / monthly
    private Float average(Period period, Long userId) {
        // bad argument error
        if (period == null) {
            throw new IllegalArgumentException("Period cannot be null");
        }

        LocalDate end = LocalDate.now();
        LocalDate start = null;
        LocalDate prev = null;

        if (period == Period.WEEKLY) start = end.minusDays(7);
        if (period == Period.MONTHLY) start = end.minusDays(30);

        List<Logs> logs = logsRepo.findAllByUserIdAndDateBetween(userId, start, end);
        float sum = 0f;
        int count = 0;

        for (Logs saved : logs) {
            sum += saved.getAmount();

            // check for dupe dates
            if (prev == null || !(prev.equals(saved.getDate()))) {
                count++;
                prev = saved.getDate();
            }
        }
        // make sure count is not zero so it doesnt crash
        return count == 0 ? 0f : sum / count;
    }
}
