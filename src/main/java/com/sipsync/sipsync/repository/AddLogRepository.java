package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
    public interface AddLogRepository extends JpaRepository<Logs, Long> {
    // get latest value user added
    Logs findTopByUserIdOrderByIdDesc(Long userId);

    List<Logs> findByUserId(Long userid);


   List<Logs> findByUserIdAndTime(Long userId, String time);


}
