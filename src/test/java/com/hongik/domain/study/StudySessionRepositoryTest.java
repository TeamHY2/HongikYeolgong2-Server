package com.hongik.domain.study;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
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

    @DisplayName("유저의 특정 날짜(Day)에 대한 공부 시간 조회")
    @Test
    void getStudyDurationForDay() {
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
        Long studyDurationForDay = studySessionRepository.getStudyDurationForDay(user.getId(), 2024, 9, 19);

        // then
        assertThat(studyDurationForDay).isEqualTo(120L);
    }

    @DisplayName("유저의 특정 날짜(Month)에 대한 공부 시간 조회")
    @Test
    void getStudyDurationForMonth() {
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
        Long studyDurationForDay = studySessionRepository.getStudyDurationForMonth(user.getId(), 2024, 9);

        // then
        assertThat(studyDurationForDay).isEqualTo(120L);
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
        List<Object[]> result = studySessionRepository.getStudyCountByAll(user.getId());

        // then
        assertThat(result).hasSize(3)
                .extracting(row -> ((java.sql.Date) row[0]).toLocalDate(), row -> (Long) row[1])
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
}