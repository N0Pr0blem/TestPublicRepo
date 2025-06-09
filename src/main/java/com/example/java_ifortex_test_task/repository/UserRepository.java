package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            SELECT u.* FROM users u 
            JOIN sessions s ON u.id=s.user_id 
            AND s.device_type=?1
            ORDER BY s.started_at_utc DESC;
            """, nativeQuery = true)
    List<User> getUsersWithAtLeastOneSessionWithDeviceTypeCode(int code);

    @Query(value = """
            SELECT u.* FROM users u 
            JOIN(
            SELECT user_id FROM sessions
            GROUP BY user_id
            ORDER BY COUNT(id) DESC
            LIMIT 1
            ) max_user ON u.id=max_user.user_id;
            """, nativeQuery = true)
    User getUserWithMostSessions();
}
