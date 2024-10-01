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

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

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

    public List<StudyCountResponse> getStudyCountOfWeek(LocalDate today, final Long userId) {
        // 이번 주의 시작일 (월요일)
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));

        // 이번 주의 종료일 (일요일)
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        List<LocalDate> dates = new ArrayList<>();

        // 시작일부터 종료일까지 날짜 출력
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            dates.add(date);
        }

        // 일주일을 Map에 담는다.
        Map<LocalDate, Long> week = new LinkedHashMap<>();
        for (LocalDate date : dates) {
            week.put(date, 0L);
        }

        // Controller에서 받은 한 주의 날짜의 공부 횟수를 조회한다. -> 공부 횟수가 존재하지 않으면 값이 나오지 않는다.
        List<Object[]> results = studySessionRepository.getStudyCountByWeek(userId, dates);

        // 위 쿼리를 이용하여 Map에 존재하면 쿼리 조회 결과(공부 횟수)로 덮어씌운다.
        for (Object[] result : results) {
            LocalDate localDate = ((Date) result[0]).toLocalDate();
            if (week.containsKey(localDate)) {
                week.put(localDate, (Long) result[1]);
            }
        }

        return week.entrySet().stream()
                .map(entry -> StudyCountResponse.of(entry.getKey(), entry.getValue()))
                .collect(toList());
    }
}
