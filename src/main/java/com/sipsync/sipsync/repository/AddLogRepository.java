package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.AddLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface AddLogRepository  extends JpaRepository<AddLog, Long> {
    }

