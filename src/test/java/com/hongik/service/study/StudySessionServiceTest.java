package com.hongik.service.study;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.study.request.EndStudySessionRequest;
import com.hongik.dto.study.request.StudySessionCreateRequest2;
import com.hongik.dto.study.response.*;
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


    @DisplayName("새로운 스터디 세션을 시작한다 (createStudy2)")
    @Test
    void createStudy() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = createUser("username", "password", "nickname", "컴퓨터공학부");
        userRepository.save(user);

        StudySessionCreateRequest2 request = StudySessionCreateRequest2.builder()
                .startTime(now)
                .build();

        // when
        StudySessionStartResponse response = studySessionService.createStudy(request, user.getId());

        // then
        assertThat(response.getStudySessionId()).isNotNull();

        StudySession saved = studySessionRepository.findById(response.getStudySessionId())
                .orElseThrow();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
        assertThat(saved.getStartTime()).isEqualTo(now);
        assertThat(saved.getEndTime()).isNull(); // 시작만 하고 끝내지 않음
        assertThat(saved.isStudyStatus()).isTrue();
    }

    @DisplayName("스터디 세션 종료 시 endTime을 저장하고 상태를 false로 만든다")
    @Test
    void updateStudy() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2024, 7, 14, 10, 0);
        LocalDateTime endTime = startTime.plusHours(1);

        User user = createUser("username", "password", "nickname", "조소과");
        userRepository.save(user);

        StudySession studySession = StudySession.builder()
                .user(user)
                .startTime(startTime)
                .studyStatus(true)
                .build();
        studySessionRepository.save(studySession);

        EndStudySessionRequest request = EndStudySessionRequest.builder()
                .studySessionId(studySession.getId())
                .endTime(endTime)
                .build();

        // when
        EndStudySessionResponse response = studySessionService.updateStudy(request);

        // then
        assertThat(response.getStudySessionId()).isEqualTo(studySession.getId());
        StudySession updated = studySessionRepository.findById(studySession.getId()).orElseThrow();
        assertThat(updated.getEndTime()).isEqualTo(endTime);
        assertThat(updated.isStudyStatus()).isFalse();
    }

    @DisplayName("오늘 하루 동안 공부 중이거나 공부한 유저 리스트를 반환한다")
    @Test
    void getStudyingUsers() {
        // given
        LocalDate today = LocalDate.now();
        LocalDateTime startTime = today.atTime(8, 0);
        LocalDateTime endTime = today.atTime(10, 30);

        User user = createUser("username", "password", "nickname", "컴퓨터공학부");
        userRepository.save(user);

        StudySession session1 = createStudySession(user, startTime, endTime, false);
        studySessionRepository.save(session1);

        // when
        List<StudyingUserResponse> responses = studySessionService.getStudyingUsers();

        // then
        assertThat(responses).isNotEmpty();
        StudyingUserResponse response = responses.get(0);
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getStudyDuration()).isEqualTo(java.time.LocalTime.of(2, 30));
        assertThat(response.isStudyStatus()).isFalse();
    }

//    @DisplayName("원하는 날짜에 대한 랭킹을 조회한다. ex) 9월 3주차 YearWeek = 202438")
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
//        LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 12, 10);
//        LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
//        LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
//        StudySession studySession1 = createStudySession(user1, startTime1, endTime1);
//        StudySession studySession2 = createStudySession(user2, startTime2, endTime2);
//        studySessionRepository.saveAll(List.of(studySession1, studySession2));
//
//        // when
//        WeeklyRankingResponse result = studySessionService.getStudyDurationRanking(202438);
//
//        // then
//        assertThat(result)
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

    private StudySession createStudySession(User user, final LocalDateTime startTime, final LocalDateTime endTime, final boolean studyStatus) {
        return StudySession.builder()
                .user(user)
                .startTime(startTime)
                .endTime(endTime)
                .studyStatus(studyStatus)
                .build();
    }
}
