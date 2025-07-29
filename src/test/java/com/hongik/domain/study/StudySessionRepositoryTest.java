package com.hongik.domain.study;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.study.response.StudyCountLocalDate;
import com.hongik.dto.study.response.UserStudyDuration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class StudySessionRepositoryTest {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("특정 날짜에 유저별 공부 시간 및 최근 상태 조회")
    @Test
    void getUserStudyDurationForDayAsSeconds() {
        // given
        User user1 = createUser("user1", "pw", "닉1", "컴공");
        User user2 = createUser("user2", "pw", "닉2", "경영");
        userRepository.saveAll(List.of(user1, user2));

        LocalDate date = LocalDate.of(2024, 9, 19);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        // user1: 2시간
        StudySession u1s1 = createStudySession(user1,
                LocalDateTime.of(2024, 9, 19, 10, 0),
                LocalDateTime.of(2024, 9, 19, 11, 0),
                true);

        StudySession u1s2 = createStudySession(user1,
                LocalDateTime.of(2024, 9, 19, 20, 0),
                LocalDateTime.of(2024, 9, 19, 21, 0),
                false);

        // user2: 1시간
        StudySession u2s1 = createStudySession(user2,
                LocalDateTime.of(2024, 9, 19, 9, 0),
                LocalDateTime.of(2024, 9, 19, 10, 0),
                true);

        studySessionRepository.saveAll(List.of(u1s1, u1s2, u2s1));

        // when
        List<UserStudyDuration> result = studySessionRepository.getUserStudyDurationForDayAsSeconds(startOfDay, endOfDay);

        // then
        assertThat(result).hasSize(2);

        UserStudyDuration user1Result = result.stream()
                .filter(r -> r.getUserId().equals(user1.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(user1Result.getNickname()).isEqualTo("닉1");
        assertThat(user1Result.getTotalSeconds()).isEqualTo(7200L);
        assertThat(user1Result.getStudyStatus()).isEqualTo(false);

        UserStudyDuration user2Result = result.stream()
                .filter(r -> r.getUserId().equals(user2.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(user2Result.getNickname()).isEqualTo("닉2");
        assertThat(user2Result.getTotalSeconds()).isEqualTo(3600L);
        assertThat(user2Result.getStudyStatus()).isEqualTo(true);
    }



    @DisplayName("유저의 특정 연도(Year)에 대한 공부 시간 조회 (결과 SECOND)")
    @Test
    void getStudyDurationForYearAsSeconds() {
        // given
        User user = createUser("username", "password", "nickname", "디자인학부");
        userRepository.save(user);

        LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
        StudySession studySession1 = createStudySession(user, startTime1, endTime1);
        StudySession studySession2 = createStudySession(user, startTime2, endTime2);
        studySessionRepository.saveAll(List.of(studySession1, studySession2));

        // when
        Long studyDurationForDay = studySessionRepository.getStudyDurationForYearAsSeconds(user.getId(), 2024);

        // then
        assertThat(studyDurationForDay).isEqualTo(7200L);
    }

    @DisplayName("유저의 특정 날짜(Month)에 대한 공부 시간 조회 (결과 SECOND)")
    @Test
    void getStudyDurationForMonthAsSeconds() {
        // given
        User user = createUser("username", "password", "nickname", "디자인학부");
        userRepository.save(user);

        LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 20, 20, 10);
        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 20, 21, 10);
        StudySession studySession1 = createStudySession(user, startTime1, endTime1);
        StudySession studySession2 = createStudySession(user, startTime2, endTime2);
        studySessionRepository.saveAll(List.of(studySession1, studySession2));

        // when
        Long studyDurationForDay = studySessionRepository.getStudyDurationForMonthAsSeconds(user.getId(), 2024, 9);

        // then
        assertThat(studyDurationForDay).isEqualTo(7200L);
    }

    @DisplayName("유저의 특정 날짜(Day)에 대한 공부 시간 조회 (결과 SECOND)")
    @Test
    void getStudyDurationForDayAsSeconds() {
        // given
        User user = createUser("username", "password", "nickname", "디자인학부");
        userRepository.save(user);

        LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
        StudySession studySession1 = createStudySession(user, startTime1, endTime1);
        StudySession studySession2 = createStudySession(user, startTime2, endTime2);
        studySessionRepository.saveAll(List.of(studySession1, studySession2));

        // when
        Long studyDurationForDay = studySessionRepository.getStudyDurationForDayAsSeconds(user.getId(), 2024, 9, 19);

        // then
        assertThat(studyDurationForDay).isEqualTo(7200L);
    }

    @DisplayName("유저의 특정 학기(Semester)에 대한 공부 시간 조회 (결과 SECOND)")
    @Test
    void getStudyDurationForSemesterAsSeconds() {
        // given
        User user = createUser("username", "password", "nickname", "디자인학부");
        userRepository.save(user);

        // 9월의 Semester 값은 second
        LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
        StudySession studySession1 = createStudySession(user, startTime1, endTime1);
        StudySession studySession2 = createStudySession(user, startTime2, endTime2);
        studySessionRepository.saveAll(List.of(studySession1, studySession2));

        // when
        Long studyDurationForSemester = studySessionRepository.getStudyDurationForSemesterAsSeconds(user.getId(), 2024, "second");

        // then
        assertThat(studyDurationForSemester).isEqualTo(7200L);
    }

    @DisplayName("유저의 특정 날짜(Year, Month)에 대한 공부 횟수 조회")
    @Test
    void getStudyCountByMonth() {
        // given
        User user = createUser("username", "password", "nickname", "디자인학부");
        userRepository.save(user);

        LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
        LocalDateTime startTime3 = LocalDateTime.of(2024, 9, 20, 20, 10);
        LocalDateTime endTime3 = LocalDateTime.of(2024, 9, 20, 21, 10);
        StudySession studySession1 = createStudySession(user, startTime1, endTime1);
        StudySession studySession2 = createStudySession(user, startTime2, endTime2);
        StudySession studySession3 = createStudySession(user, startTime3, endTime3);
        studySessionRepository.saveAll(List.of(studySession1, studySession2, studySession3));

        // when
        List<Object[]> result = studySessionRepository.getStudyCountByMonth(user.getId(), 2024, 9);

        // then
        assertThat(result).hasSize(2)
                .extracting(row -> ((java.sql.Date) row[0]).toLocalDate(), row -> (Long) row[1])
                .containsExactlyInAnyOrder(
                        tuple(LocalDate.of(2024, 9, 19), 2L),
                        tuple(LocalDate.of(2024, 9, 20), 1L)
                );
    }

    @DisplayName("유저에 모든 날짜에 대한 공부 횟수 조회")
    @Test
    void getStudyCountByAll() {
        // given
        User user = createUser("username", "password", "nickname", "디자인학부");
        userRepository.save(user);

        LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
        LocalDateTime startTime3 = LocalDateTime.of(2024, 9, 20, 20, 10);
        LocalDateTime endTime3 = LocalDateTime.of(2024, 9, 20, 21, 10);
        LocalDateTime startTime4 = LocalDateTime.of(2024, 8, 20, 20, 10);
        LocalDateTime endTime4 = LocalDateTime.of(2024, 8, 20, 21, 10);
        StudySession studySession1 = createStudySession(user, startTime1, endTime1);
        StudySession studySession2 = createStudySession(user, startTime2, endTime2);
        StudySession studySession3 = createStudySession(user, startTime3, endTime3);
        StudySession studySession4 = createStudySession(user, startTime4, endTime4);
        studySessionRepository.saveAll(List.of(studySession1, studySession2, studySession3, studySession4));

        // when
        List<StudyCountLocalDate> result = studySessionRepository.getStudyCountByAll(user.getId());

        // then
        assertThat(result).hasSize(3)
                .extracting("date", "studyCount")
                .containsExactlyInAnyOrder(
                        tuple(LocalDate.of(2024, 9, 19), 2L),
                        tuple(LocalDate.of(2024, 9, 20), 1L),
                        tuple(LocalDate.of(2024, 8, 20), 1L)
                );
    }

    private User createUser(final String username, final String password, final String nickname, final String department) {
        return User.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .department(department)
                .nickname(nickname)
                .build();
    }

    private StudySession createStudySession(User user, final LocalDateTime startTime, final LocalDateTime endTime) {
        return StudySession.builder()
                .user(user)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    private StudySession createStudySession(User user, LocalDateTime startTime, LocalDateTime endTime, boolean studyStatus) {
        return StudySession.builder()
                .user(user)
                .startTime(startTime)
                .endTime(endTime)
                .studyStatus(studyStatus)
                .build();
    }
}
