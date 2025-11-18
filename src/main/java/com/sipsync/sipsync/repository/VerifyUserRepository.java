package com.sipsync.sipsync.repository;
import com.sipsync.sipsync.model.Verify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface VerifyUserRepository extends JpaRepository<Verify, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Verify v SET v.isVerified = true WHERE v.id = :id")
    void verifyById(@Param("id") Long id);
}
