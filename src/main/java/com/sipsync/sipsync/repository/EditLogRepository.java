package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.EditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditLogRepository extends JpaRepository<EditLog, Long> {
}
