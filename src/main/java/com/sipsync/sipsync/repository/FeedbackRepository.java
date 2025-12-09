package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
