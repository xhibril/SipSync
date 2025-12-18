package com.sipsync.sipsync.service;

import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class StatsService {

@Autowired
LogsRepository addRepo;

    public Integer totals(final String type, Long userId) {

        List<Logs> savedAmounts = addRepo.findByUserId(userId);
        LocalDate today = LocalDate.now();
        LocalDate prevLogDate = null;
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        String date = String.valueOf(today);
        String day = String.valueOf(dayOfWeek);

        int total = 0;
        int avg = 0;


        switch (type) {
            case "DAILY":
                for (Logs saved : savedAmounts) {

                    if (String.valueOf(today).equals(saved.getDate())) {
                        total += saved.getAmount();
                    }
                }
                return total;

            case "WEEKLY":
                LocalDate sevenDaysAgo = today.minusDays(-7);
                int weeklyDaysCount = 0;

                for (Logs saved : savedAmounts) {

                    // convert string to local date obj
                    LocalDate currLogDate = LocalDate.parse(saved.getDate());

                    if (!(sevenDaysAgo.isBefore(currLogDate) && !(today.isAfter(currLogDate)))) {

                        // if not a dupe, increment weekly days count
                        if (prevLogDate == null || !(prevLogDate.equals(currLogDate))) {
                            weeklyDaysCount++;
                            prevLogDate = currLogDate;
                        }
                        avg += saved.getAmount();
                    }
                }


                if(avg == 0 || weeklyDaysCount == 0){
                    return 0;
                }

                return avg / weeklyDaysCount;


            case "MONTHLY":
                LocalDate thirtyDaysAgo = today.minusDays(-30);
                int monthlyDaysCount = 0;

                for (Logs saved : savedAmounts) {
                    // convert string to local date obj
                    LocalDate currLogDate = LocalDate.parse(saved.getDate());

                    if (!(thirtyDaysAgo.isBefore(currLogDate) && !(today.isAfter(currLogDate)))) {

                        // if not a dupe, increment monthly days count
                        if (prevLogDate == null || !(prevLogDate.equals(currLogDate))) {
                            monthlyDaysCount++;
                            prevLogDate = currLogDate;
                        }
                        avg += saved.getAmount();
                    }
                }
                if (avg == 0 || monthlyDaysCount == 0){
                    return 0;
                }
                return avg / monthlyDaysCount;
        }
        return 0;
    }
}
