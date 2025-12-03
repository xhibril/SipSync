package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
    public interface AddLogRepository extends JpaRepository<Logs, Long> {
    // get latest value user added
    Logs findTopByUserIdOrderByIdDesc(Long userId);

    List<Logs> findByUserId(Long userid);


   List<Logs> findByUserIdAndTime(Long userId, String time);



    @Transactional
    @Modifying
    @Query("DELETE FROM Logs u WHERE u.userId = :userId")
    void deleteUserDataLogs(@Param("userId") Long userId );
}
