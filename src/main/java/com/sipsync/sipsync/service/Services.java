package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.Goal;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.model.Edit;
import com.sipsync.sipsync.repository.AddLogRepository;
import com.sipsync.sipsync.repository.EditLogRepository;
import com.sipsync.sipsync.repository.SetGoalRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class Services {

    @Autowired private AddLogRepository addRepo;
    @Autowired private SetGoalRepository goalRepo;
    @Autowired private EditLogRepository editRepo;
    @Autowired private Cookies cookiesService;
    @Autowired private TokenService tokenService;


    // add amount
    public void addLog(int amount, HttpServletRequest req){

       String token =  cookiesService.getTokenByCookie(req);
       Long userId = tokenService.extractId(token);

        Logs log = new Logs();
        LocalDate today = LocalDate.now();
        log.setUserId(userId);

        log.setAmount(amount);
        log.setTime(String.valueOf(today));
        addRepo.save(log);
    }

    
    public TotalsRecord totals(final String type, HttpServletRequest req) {
        String token = cookiesService.getTokenByCookie(req);
        Long userId = tokenService.extractId(token);
        List<Logs> savedAmounts = addRepo.findByUserId(userId);
        LocalDate today = LocalDate.now();
        LocalDate prevLogDate = null;
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        String date =  String.valueOf(today);
        String day = String.valueOf(dayOfWeek);


        int sum = 0;

        switch (type) {
            case "DAILY":
                for (Logs saved : savedAmounts) {

                    if (String.valueOf(today).equals(saved.getTime())) {
                        sum += saved.getAmount();
                    }
                }
                return new TotalsRecord(sum, date, day, 0);

            case "WEEKLY":
                LocalDate sevenDaysAgo = today.minusDays(-7);
                int weeklyDaysCount = 0;

                for (Logs saved : savedAmounts) {

                    // convert string to local date obj
                    LocalDate currLogDate = LocalDate.parse(saved.getTime());

                    if (!(sevenDaysAgo.isBefore(currLogDate) && !(today.isAfter(currLogDate)))) {

                        // if not a dupe, increment weekly days count
                        if(prevLogDate == null || !(prevLogDate.equals(currLogDate))){
                            weeklyDaysCount++;
                            prevLogDate = currLogDate;
                        }
                        sum += saved.getAmount();
                    }
                }
                return new TotalsRecord(sum, date, day, weeklyDaysCount);


            case "MONTHLY":
               LocalDate thirtyDaysAgo = today.minusDays(-30);
               int monthlyDaysCount = 0;

                for (Logs saved : savedAmounts) {
                    // convert string to local date obj
                    LocalDate currLogDate = LocalDate.parse(saved.getTime());

                    if (!(thirtyDaysAgo.isBefore(currLogDate) && !(today.isAfter(currLogDate)))) {

                        // if not a dupe, increment monthly days count
                        if(prevLogDate == null || !(prevLogDate.equals(currLogDate))){
                            monthlyDaysCount++;
                            prevLogDate = currLogDate;
                        }
                        sum += saved.getAmount();
                    }
                }
                return new TotalsRecord(sum, date, day, monthlyDaysCount);
        }
            return null;
        }






    public void editLatestValue(int value){

        Logs last = addRepo.findTopByOrderByIdDesc();
        Long id = last.getId();

        Edit edit = new Edit();
        edit.setAmount(value);
        edit.setId(id);

        editRepo.save(edit);
    }



    public void setGoal(int amount, HttpServletRequest req){

        String token = cookiesService.getTokenByCookie(req);
        Long userId = tokenService.extractId(token);
        Goal goal = new Goal();
        goal.setGoal(amount);
        goal.setUserId(userId);
        goalRepo.save(goal);
    }


    public GoalRecord getSetGoal(HttpServletRequest req) {

        String token = cookiesService.getTokenByCookie(req);
        Long userId = tokenService.extractId(token);

        Optional<Goal> result = goalRepo.findById(userId);

        // check if sum is inside the wrapper optional
        if (result.isPresent()) {
            // return if found
            Goal goal = result.get();
            return new GoalRecord(goal.getGoal(), goal.getUserId());

        } else {
            return null;
        }
    }


}



