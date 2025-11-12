package com.sipsync.sipsync.repository;

import com.sipsync.sipsync.model.Edit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditLogRepository extends JpaRepository<Edit, Long> {
}
