package com.hongik.service.study;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.StudyCountResponse;
import com.hongik.dto.study.response.StudyDurationResponse;
import com.hongik.dto.study.response.StudySessionResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StudySessionServiceTest {

    @Autowired
    private StudySessionService studySessionService;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        studySessionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("열람실 이용 종료한다. 시작 시간과 종료 시간을 저장한다.")
    @Test
    void createStudy() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = createUser("username", "password", "nickname", "department");
        userRepository.save(user);

        StudySessionCreateRequest request = StudySessionCreateRequest.builder()
                .startTime(now)
                .endTime(now.plusMinutes(5))
                .build();

        // when
        StudySessionResponse studySessionResponse = studySessionService.createStudy(request, user.getId());

        // then
        assertThat(studySessionResponse.getId()).isNotNull();
        assertThat(studySessionResponse)
                .extracting("startTime", "endTime")
                .containsExactlyInAnyOrder(now, now.plusMinutes(5));
    }
    
    @DisplayName("서버 시간 기준으로 유저의 날짜(Year, Month, Day, Semester)에 대한 공부 시간 조회")
    @Test
    void getStudyDuration() {
        // given
        LocalDate now = LocalDate.of(2024, 9, 20);
        // 9월은 2학기다.
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
        StudyDurationResponse studyDurationResponse = studySessionService.getStudyDuration(now, user.getId());

        // then
        assertThat(studyDurationResponse).isNotNull();
        assertThat(studyDurationResponse)
                .extracting("studyDurationWithYear", "studyDurationWithMonth", "studyDurationWithDay", "studyDurationWithSemester")
                .containsExactly(180L, 180L, 60L, 180L);
    }

    @DisplayName("유저의 특정 날짜(Month, Day)에 대한 공부 횟수 조회")
    @Test
    void getStudyCount() {
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
        List<StudyCountResponse> result = studySessionService.getStudyCount(2024, 9, user.getId());

        // then
        assertThat(result).hasSize(2)
                .extracting("date", "studyCount")
                .containsExactlyInAnyOrder(
                        tuple(LocalDate.of(2024, 9, 19), 2L),
                        tuple(LocalDate.of(2024, 9, 20), 1L)
                );
    }

    @DisplayName("현재 날짜 기준으로 주단위 월, 일, 공부 횟수를 가져옵니다. 순서 보장")
    @Test
    void getStudyCountOfWeek() {
        // given
        User user = createUser("username", "password", "nickname", "디자인학부");
        userRepository.save(user);

        // 9월 16일 월요일 기준 16 ~ 22일 데이터가 출력된다.
        LocalDate today = LocalDate.of(2024, 9, 16);

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
        List<StudyCountResponse> result = studySessionService.getStudyCountOfWeek(today, user.getId());

        // then
        assertThat(result).hasSize(7)
                .extracting("date", "studyCount")
                .containsExactly(
                        tuple(LocalDate.of(2024, 9, 16), 0L),
                        tuple(LocalDate.of(2024, 9, 17), 0L),
                        tuple(LocalDate.of(2024, 9, 18), 0L),
                        tuple(LocalDate.of(2024, 9, 19), 2L),
                        tuple(LocalDate.of(2024, 9, 20), 1L),
                        tuple(LocalDate.of(2024, 9, 21), 0L),
                        tuple(LocalDate.of(2024, 9, 22), 0L)

                );
    }

//    @DisplayName("원하는 날짜에 대한 랭킹을 조회한다. ex) 9월 2주차")
//    @Test
//    void getStudyDurationRanking() {
//        // given
//        User user1 = createUser("username1", "password", "nickname", "디자인학부");
//        User user2 = createUser("username2", "password", "nickname", "건축학부");
//        User user3 = createUser("username3", "password", "nickname", "컴퓨터공학");
//        User user4 = createUser("username4", "password", "nickname", "조소과");
//        userRepository.saveAll(List.of(user1, user2, user3, user4));
//
//        // 9월 16일 월요일 기준 16 ~ 22일 데이터가 출력된다.
//        LocalDate today = LocalDate.of(2024, 9, 16);
//
//        LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
//        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
//        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
//        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
//        LocalDateTime startTime3 = LocalDateTime.of(2024, 9, 20, 20, 10);
//        LocalDateTime endTime3 = LocalDateTime.of(2024, 9, 20, 21, 10);
//        StudySession studySession1 = createStudySession(user, startTime1, endTime1);
//        StudySession studySession2 = createStudySession(user, startTime2, endTime2);
//        StudySession studySession3 = createStudySession(user, startTime3, endTime3);
//        studySessionRepository.saveAll(List.of(studySession1, studySession2, studySession3));
//
//        // when
//        List<StudyCountResponse> result = studySessionService.getStudyCountOfWeek(today, user.getId());
//
//        // then
//        assertThat(result).hasSize(7)
//                .extracting("date", "studyCount")
//                .containsExactly(
//                        tuple(LocalDate.of(2024, 9, 16), 0L),
//                        tuple(LocalDate.of(2024, 9, 17), 0L),
//                        tuple(LocalDate.of(2024, 9, 18), 0L),
//                        tuple(LocalDate.of(2024, 9, 19), 2L),
//                        tuple(LocalDate.of(2024, 9, 20), 1L),
//                        tuple(LocalDate.of(2024, 9, 21), 0L),
//                        tuple(LocalDate.of(2024, 9, 22), 0L)
//
//                );
//    }

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