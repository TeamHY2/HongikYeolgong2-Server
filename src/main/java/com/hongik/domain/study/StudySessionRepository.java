package com.hongik.domain.study;

import com.hongik.dto.study.response.StudyCount;
import com.hongik.dto.study.response.StudyCountLocalDate;
import com.hongik.dto.study.response.StudyRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    /**
     * 2024년 10월 1일에 대한 공부 시간을 조회한다.
     */
    @Query(value = "SELECT IFNULL(SUM(TIMESTAMPDIFF(SECOND, s.start_time, s.end_time)), 0) " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "AND YEAR(s.start_time) = :year " +
            "AND MONTH(s.start_time) = :month " +
            "AND DAY(s.start_time) = :day", nativeQuery = true)
    Long getStudyDurationForDayAsSeconds(@Param("userId") Long userId,
                                @Param("year") int year,
                                @Param("month") int month,
                                @Param("day") int day);

    /**
     * 2024년 10월에 대한 공부 시간을 조회한다.
     */
    @Query(value = "SELECT IFNULL(SUM(TIMESTAMPDIFF(SECOND, s.start_time, s.end_time)), 0)" +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "AND YEAR(s.start_time) = :year " +
            "AND MONTH(s.start_time) = :month", nativeQuery = true)
    Long getStudyDurationForMonthAsSeconds(@Param("userId") Long userId,
                                  @Param("year") int year,
                                  @Param("month") int month);

    /**
     * 2024년에 대한 공부 시간을 조회한다.
     */
    @Query(value = "SELECT IFNULL(SUM(TIMESTAMPDIFF(SECOND, s.start_time, s.end_time)), 0)" +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "AND YEAR(s.start_time) = :year", nativeQuery = true)
    Long getStudyDurationForYearAsSeconds(@Param("userId") Long userId,
                                 @Param("year") int year);

    /**
     * 2024년 학기에 대한 공부 시간을 조회한다.
     */
    @Query(value = "SELECT " +
            "CASE " +
            "WHEN :semester = 'winter' THEN IFNULL(SUM(CASE WHEN YEAR(s.start_time) = :year AND MONTH(s.start_time) IN (1, 2) THEN TIMESTAMPDIFF(SECOND, s.start_time, s.end_time) ELSE 0 END), 0) " +
            "WHEN :semester = 'first' THEN IFNULL(SUM(CASE WHEN YEAR(s.start_time) = :year AND MONTH(s.start_time) IN (3, 4, 5, 6) THEN TIMESTAMPDIFF(SECOND, s.start_time, s.end_time) ELSE 0 END), 0) " +
            "WHEN :semester = 'summer' THEN IFNULL(SUM(CASE WHEN YEAR(s.start_time) = :year AND MONTH(s.start_time) IN (7, 8) THEN TIMESTAMPDIFF(SECOND, s.start_time, s.end_time) ELSE 0 END), 0)" +
            "WHEN :semester = 'second' THEN IFNULL(SUM(CASE WHEN YEAR(s.start_time) = :year AND MONTH(s.start_time) IN (9, 10, 11, 12) THEN TIMESTAMPDIFF(SECOND, s.start_time, s.end_time) ELSE 0 END), 0) " +
            "ELSE 0 END AS semesterStudyTime " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId", nativeQuery = true)
    Long getStudyDurationForSemesterAsSeconds(@Param("userId") Long userId,
                                     @Param("year") int year,
                                     @Param("semester") String semester);

    @Query(value = "SELECT DATE(s.start_time) AS date, COUNT(*) AS studyCount " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "AND YEAR(s.start_time) = :year " +
            "AND MONTH(s.start_time) = :month " +
            "GROUP BY DATE(s.start_time)", nativeQuery = true)
    List<Object[]> getStudyCountByMonth(@Param("userId") Long userId,
                                        @Param("year") int year,
                                        @Param("month") int month);

    /**
     * 20xx년 전체 공부 횟수를 조회한다.
     * 캘린더에 공부 횟수로 색칠한다.
     */
    @Query(value = "SELECT DATE(s.start_time) AS date, COUNT(*) AS studyCount " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "GROUP BY DATE(s.start_time)", nativeQuery = true)
    List<StudyCountLocalDate> getStudyCountByAll(@Param("userId") Long userId);

    /**
     * 20xx년 x월 x일에 대한 월요일~일요일 공부 횟수를 조회한다.
     * 홈 화면 일주일 캘린더에 공부 횟수로 색칠한다.
     * 반환값은 LocalDate, Long
     * @return StudyCount(LocalDate, Long)
     */
    @Query(value = "SELECT DATE(s.start_time) AS date, COUNT(*) AS studyCount " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "and DATE(s.start_time) in :dates " +
            "GROUP BY DATE(s.start_time)", nativeQuery = true)
    List<StudyCount> getStudyCountByWeek(@Param("userId") Long userId, List<LocalDate> dates);

    /**
     * 2024년 10월 01일 기준
     * 2024년 공부 시간
     * 2024년 10월 공부시간
     * 2024년 10월 1일 공부시간을 측정한다.
     */
    @Query(value = "SELECT " +
            "SUM(CASE WHEN YEAR(s.start_time) = :year THEN TIMESTAMPDIFF(MINUTE, s.start_time, s.end_time) " +
            "ELSE 0 " +
            "END) as yearly " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId", nativeQuery = true)
    Long getTotalStudyTime(@Param("userId") Long userId, @Param("year") int year);

    /**
     * 주차 데이터를 매개변수로, 랭킹을 조회한다.
     */
    @Query(value = "SELECT d.department_name AS department, " +
            "COALESCE(SUM(TIMESTAMPDIFF(SECOND, s.start_time, s.end_time)), 0) AS seconds, " +
            "RANK() OVER (ORDER BY COALESCE(SUM(TIMESTAMPDIFF(SECOND, s.start_time, s.end_time)), 0) DESC) AS ranking " +
            "FROM department d " +
            "LEFT JOIN users u ON u.department = d.department_name " +
            "LEFT JOIN study_session s ON u.id = s.user_id " +
            "AND YEARWEEK(s.start_time, 1) = :weekYear " +
            "GROUP BY d.department_name", nativeQuery = true)
    List<StudyRanking> getWeeklyStudyTimeRankingByDepartment(@Param("weekYear") int weekYear);
}
