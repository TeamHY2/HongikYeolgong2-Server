package com.hongik.service.study;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.StudyCountResponse;
import com.hongik.dto.study.response.StudyDurationResponse;
import com.hongik.dto.study.response.StudySessionResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;

    private final UserRepository userRepository;

    @Transactional
    public StudySessionResponse createStudy(StudySessionCreateRequest request, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        StudySession savedStudySession = studySessionRepository.save(request.toEntity(user));

        return StudySessionResponse.of(savedStudySession);
    }

    public StudyDurationResponse getStudyDuration(final int year, final int month, final int day, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        Long studyDurationForDay = studySessionRepository.getStudyDurationForDay(userId, year, month, day);
        Long studyDurationForMonth = studySessionRepository.getStudyDurationForMonth(userId, year, month);
        return StudyDurationResponse.of(studyDurationForDay, studyDurationForMonth);
    }

    public List<StudyCountResponse> getStudyCount(final int year, final int month, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));
        
        List<Object[]> results = studySessionRepository.getStudyCountByMonth(userId, year, month);
        return results.stream()
                .map(result -> StudyCountResponse.of(
                        ((java.sql.Date) result[0]).toLocalDate(),
                        ((Number) result[1]).longValue()
                ))
                .collect(toList());
    }

    public List<StudyCountResponse> getStudyCountAll(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        List<Object[]> results = studySessionRepository.getStudyCountByAll(userId);
        return results.stream()
                .map(result -> StudyCountResponse.of(
                        ((java.sql.Date) result[0]).toLocalDate(),
                        ((Number) result[1]).longValue()
                ))
                .collect(toList());
    }
}
