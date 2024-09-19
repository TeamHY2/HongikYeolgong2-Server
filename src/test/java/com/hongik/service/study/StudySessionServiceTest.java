package com.hongik.service.study;

import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.StudySessionResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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

    private User createUser(final String username, final String password, final String nickname, final String department) {
        return User.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .department(department)
                .nickname(nickname)
                .build();
    }
}