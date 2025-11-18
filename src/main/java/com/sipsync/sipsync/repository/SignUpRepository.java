package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpRepository extends JpaRepository<User, Long> {
}
