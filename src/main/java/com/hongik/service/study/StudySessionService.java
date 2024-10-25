package com.hongik.service.study;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.domain.weekly.Weekly;
import com.hongik.domain.weekly.WeeklyRepository;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.*;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;

    private final UserRepository userRepository;

    private final WeeklyRepository weeklyRepository;

    @Transactional
    public StudySessionResponse createStudy(StudySessionCreateRequest request, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        StudySession savedStudySession = studySessionRepository.save(request.toEntity(user));

        return StudySessionResponse.of(savedStudySession);
    }

    public StudyDurationResponse getStudyDuration(final LocalDate now, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        final int year = now.getYear();
        final int month = now.getMonthValue();
        final int day = now.getDayOfMonth();

        String currentSemester = getCurrentSemester(month);

        Long studyDurationWithYear = studySessionRepository.getStudyDurationForYearAsSeconds(userId, year);
        Long yearHours = studyDurationWithYear / 3600;
        Long yearMinutes = studyDurationWithYear % 3600 / 60;

        Long studyDurationWithMonth = studySessionRepository.getStudyDurationForMonthAsSeconds(userId, year, month);
        Long monthHours = studyDurationWithMonth / 3600;
        Long monthMinutes = studyDurationWithMonth % 3600 / 60;

        Long studyDurationWithDay = studySessionRepository.getStudyDurationForDayAsSeconds(userId, year, month, day);
        Long dayHours = studyDurationWithDay / 3600;
        Long dayMinutes = studyDurationWithDay % 3600 / 60;

        Long studyDurationWithSemester = studySessionRepository.getStudyDurationForSemesterAsSeconds(userId, year, currentSemester);
        Long semesterHours = studyDurationWithSemester / 3600;
        Long semesterMinutes = studyDurationWithSemester % 3600 / 60;

        return StudyDurationResponse.of(yearHours, yearMinutes, monthHours, monthMinutes, dayHours, dayMinutes, semesterHours, semesterMinutes);
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

    public WeeklyRankingResponse getStudyDurationRanking(final int yearWeek) {
        // ex) 202440 데이터를 넣는다.
        List<Object[]> results = studySessionRepository.getWeeklyStudyTimeRankingByDepartment(yearWeek);
        // 202439 데이터를 넣는다. (이전 랭킹과 비교하기 위함)
        List<Object[]> results2 = studySessionRepository.getWeeklyStudyTimeRankingByDepartment(yearWeek - 1);

        Weekly weekly = weeklyRepository.findByWeekNumber(yearWeek)
                .orElseThrow(() -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));

        // 이전 주차에 랭킹을 department, 순위를 담는다.
        Map<String, Integer> previousResult = new HashMap<>();
        for (Object[] result : results2) {
            previousResult.put((String) result[0], ((Number) result[2]).intValue());
        }

        List<StudyRankingResponse> response = new ArrayList<>();
        // 현재 results의 순위와, 이전 주차 previousResult.get("department")의 순위를 비교하여 출력한다.
        for (Object[] result : results) {
            response.add(
                    StudyRankingResponse.of(
                            (String) result[0],
                            ((Number) result[1]).longValue() / 3600,
//                            ((Number) result[1]).longValue(),
                            ((Number) result[2]).intValue(),
                            previousResult.get((String) result[0])
                    )
            );
        }
        return WeeklyRankingResponse.of(weekly.getWeekName(), response);
    }

    private String getCurrentSemester(final int month) {
        if (month >= 1 && month <= 2) {
            return "winter";
        } else if (month >= 3 && month <= 6) {
            return "first";
        } else if (month >= 7 && month <= 8) {
            return "summer";
        } else {
            return "second";
        }
    }
}
