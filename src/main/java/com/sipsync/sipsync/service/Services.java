package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.EditGoal;
import com.sipsync.sipsync.model.Goal;
import com.sipsync.sipsync.model.Logs;
import com.sipsync.sipsync.model.EditLog;
import com.sipsync.sipsync.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class Services {

    @Autowired
    private AddLogRepository addRepo;
    @Autowired
    private AddUserGoalRepository addUserGoalRepo;
    @Autowired
    private EditLogRepository editRepo;
    @Autowired
    private EditUserGoalRepository editUserGoalRepo;
    @Autowired
    private Cookies cookiesService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    UserRepository userRepo;


    // add amount
    public Long addLog(int amount, Long userId) {

        Logs log = new Logs();
        LocalDate today = LocalDate.now();
        log.setUserId(userId);

        log.setAmount(amount);
        log.setTime(String.valueOf(today));
        Logs saved = addRepo.save(log);

        return saved.getId();

    }


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

                    if (String.valueOf(today).equals(saved.getTime())) {
                        total += saved.getAmount();
                    }
                }
                return total;

            case "WEEKLY":
                LocalDate sevenDaysAgo = today.minusDays(-7);
                int weeklyDaysCount = 0;

                for (Logs saved : savedAmounts) {

                    // convert string to local date obj
                    LocalDate currLogDate = LocalDate.parse(saved.getTime());

                    if (!(sevenDaysAgo.isBefore(currLogDate) && !(today.isAfter(currLogDate)))) {

                        // if not a dupe, increment weekly days count
                        if (prevLogDate == null || !(prevLogDate.equals(currLogDate))) {
                            weeklyDaysCount++;
                            prevLogDate = currLogDate;
                        }
                        avg += saved.getAmount();
                    }
                }
                return avg / weeklyDaysCount;


            case "MONTHLY":
                LocalDate thirtyDaysAgo = today.minusDays(-30);
                int monthlyDaysCount = 0;

                for (Logs saved : savedAmounts) {
                    // convert string to local date obj
                    LocalDate currLogDate = LocalDate.parse(saved.getTime());

                    if (!(thirtyDaysAgo.isBefore(currLogDate) && !(today.isAfter(currLogDate)))) {

                        // if not a dupe, increment monthly days count
                        if (prevLogDate == null || !(prevLogDate.equals(currLogDate))) {
                            monthlyDaysCount++;
                            prevLogDate = currLogDate;
                        }
                        avg += saved.getAmount();
                    }
                }
                return avg / monthlyDaysCount;
        }
        return null;
    }




    public void setGoal(int amount, Long userId) {

        // if goal already exits edit the existing one
        Optional<Goal> result = addUserGoalRepo.findByUserId(userId);

        if (result.isPresent()) {

            EditGoal editGoal = new EditGoal();
            Goal goal = new Goal();

            goal = result.get();
            Long id = goal.getId();

            editGoal.setGoal(amount);
            editGoal.setId(id);
            editUserGoalRepo.save(editGoal);
        } else {

            Goal goal = new Goal();
            goal.setGoal(amount);
            goal.setUserId(userId);
            addUserGoalRepo.save(goal);

        }
    }


    public Float getSetGoal(Long userId) {
        Optional<Goal> result = addUserGoalRepo.findByUserId(userId);

        // check if sum is inside the wrapper optional
        if (result.isPresent()) {
            // return if found
            Goal res = result.get();
            Float goal = res.getGoal();

            return goal;
        }
        return 0f;
    }


    public void resetData(Long userId){
        addRepo.deleteUserDataLogs(userId);
        addUserGoalRepo.deleteUserDataGoal(userId);
        userRepo.deleteUserStreak(userId);
    }

}







