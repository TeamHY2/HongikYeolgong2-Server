package com.hongik.domain.study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    @Query(value = "SELECT SUM(TIMESTAMPDIFF(MINUTE, s.start_time, s.end_time)) " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "AND YEAR(s.start_time) = :year " +
            "AND MONTH(s.start_time) = :month " +
            "AND DAY(s.start_time) = :day", nativeQuery = true)
    Long getStudyDurationForDay(@Param("userId") Long userId,
                                @Param("year") int year,
                                @Param("month") int month,
                                @Param("day") int day);

    @Query(value = "SELECT SUM(TIMESTAMPDIFF(MINUTE, s.start_time, s.end_time)) " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "AND YEAR(s.start_time) = :year " +
            "AND MONTH(s.start_time) = :month", nativeQuery = true)
    Long getStudyDurationForMonth(@Param("userId") Long userId,
                                @Param("year") int year,
                                @Param("month") int month);

    @Query(value = "SELECT DATE(s.start_time) AS date, COUNT(*) AS studyCount " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "AND YEAR(s.start_time) = :year " +
            "AND MONTH(s.start_time) = :month " +
            "GROUP BY DATE(s.start_time)", nativeQuery = true)
    List<Object[]> getStudyCountByMonth(@Param("userId") Long userId,
                                                  @Param("year") int year,
                                                  @Param("month") int month);

    @Query(value = "SELECT DATE(s.start_time) AS date, COUNT(*) AS studyCount " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "GROUP BY DATE(s.start_time)", nativeQuery = true)
    List<Object[]> getStudyCountByAll(@Param("userId") Long userId);

    @Query(value = "SELECT DATE(s.start_time) AS date, COUNT(*) AS studyCount " +
            "FROM study_session s " +
            "WHERE s.user_id = :userId " +
            "and DATE(s.start_time) in :dates " +
            "GROUP BY DATE(s.start_time)", nativeQuery = true)
    List<Object[]> getStudyCountByWeek(@Param("userId") Long userId, List<LocalDate> dates);
}
