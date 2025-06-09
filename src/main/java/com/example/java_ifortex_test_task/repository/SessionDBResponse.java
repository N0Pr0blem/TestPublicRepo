package com.example.java_ifortex_test_task.repository;

import java.time.LocalDateTime;

public interface SessionDBResponse {
    Long getId();
    Integer getDeviceTypeCode();
    LocalDateTime getEndedAtUtc();
    LocalDateTime getStartedAtUtc();
    Long getUserId();
}