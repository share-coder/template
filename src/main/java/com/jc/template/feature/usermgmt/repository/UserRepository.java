package com.jc.template.feature.usermgmt.repository;

import com.jc.template.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(String userId);
    User findByEmail(String email);
    User findByEmailAndMobileNumber(String email, String mobileNumber);
    User findByVerificationCode(String verificationCode);

    @Query("update User u set u.failedAttempts=?1 where u.email=?2")
    @Modifying
    void updateByFailedAttempts(int failedAttempt, String email);

    @Query("SELECT u FROM User u " +
            "WHERE (:userId IS NULL OR u.id = :userId) " +
            "AND (:role IS NULL OR u.role = :role) " +
            "AND (:status IS NULL OR u.status = :status)")
    Page<User> findByFilters(
            @Param("userId") Integer userId,
            @Param("role") String role,
            @Param("status") String status,
            Pageable pageable
    );
    void deleteUserByUserId(String userId);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(CAST(u.id AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.userId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.dob) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.status) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchAllFields(@Param("keyword") String keyword, Pageable pageable);
}
