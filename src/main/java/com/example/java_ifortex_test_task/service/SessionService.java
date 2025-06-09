package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.SessionResponseDTO;
import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import com.example.java_ifortex_test_task.mapper.SessionMapper;
import com.example.java_ifortex_test_task.repository.SessionDBResponse;
import com.example.java_ifortex_test_task.repository.SessionRepository;
import com.example.java_ifortex_test_task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    // Returns the first (earliest) desktop Session
    public SessionResponseDTO getFirstDesktopSession() {
        return sessionMapper.toDto(
                sessionDBResponseToSession(sessionRepository.getFirstSessionOfDeviceTypeCode(DeviceType.DESKTOP.getCode()))
        );
    }

    // Returns only Sessions from Active users that were ended before 2025
    public List<SessionResponseDTO> getSessionsFromActiveUsersEndedBefore2025() {
        return sessionRepository.getSessionsFromActiveUsersEndedBeforeEndTime(
                        LocalDateTime.parse("2025-01-01 00:00:00",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ).stream()
                .map(this::sessionDBResponseToSession)
                .map(sessionMapper::toDto)
                .toList();
    }

    private Session sessionDBResponseToSession(SessionDBResponse output){
        return Session.builder()
                .id(output.getId())
                .deviceType(DeviceType.fromCode(output.getDeviceTypeCode()))
                .user(userRepository.findById(output.getUserId()).get())
                .endedAtUtc(output.getEndedAtUtc())
                .startedAtUtc(output.getStartedAtUtc())
                .build();
    }
}