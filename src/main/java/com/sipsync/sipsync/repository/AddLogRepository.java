package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface AddLogRepository  extends JpaRepository<Logs, Long> {
        Logs findTopByOrderByIdDesc();
        List<Logs> findByUserId(Long userid);
    }

