package com.hongik.service;

import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.response.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입을 완료한다.")
    @Test
    void createUser() {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("test@email.com")
                .password("password")
                .nickname("nickname")
                .department("department")
                .build();

        // when
        UserResponse userResponse = userService.signUp(request);

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "nickname", "department")
                .containsExactlyInAnyOrder("test@email.com", "nickname", "department");
    }
}