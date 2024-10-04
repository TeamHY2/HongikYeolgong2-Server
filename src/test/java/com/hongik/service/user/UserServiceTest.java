package com.hongik.service.user;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.request.NicknameRequest;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.request.UserJoinRequest;
import com.hongik.dto.user.request.UsernameRequest;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.AppException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;

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

    @DisplayName("닉네임과 학과를 입력하여 회원가입을 완료한다.")
    @Test
    void joinUser() {
        // given
        User user = joinUser("user@gmail.com");
        userRepository.save(user);

        UserJoinRequest request = UserJoinRequest.builder()
                .nickname("nickname")
                .department("department")
                .build();

        // when
        UserResponse userResponse = userService.join(request, user.getId());

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "nickname", "department")
                .containsExactlyInAnyOrder("user@gmail.com", "nickname", "department");
    }

    @DisplayName("회원가입을 진행할 때 중복된 닉네임이 존재하면 예외가 발생한다.")
    @Test
    void createUserWithDuplicationNickname() {
        // given
        final String nickname = "nickname";
        User user = createUser("test@email.com", "password", nickname);
        userRepository.save(user);

        UserCreateRequest request = UserCreateRequest.builder()
                .username("test@email.com")
                .password("password")
                .nickname(nickname)
                .department("department")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }

    @DisplayName("닉네임 중복검사를 할 때 이미 닉네임이 존재하면 예외가 발생한다.")
    @Test
    void checkNicknameDuplication() {
        // given
        final String nickname = "nickname";
        User user = createUser("test@email.com", "password", nickname);
        userRepository.save(user);

        NicknameRequest request = NicknameRequest.builder()
                .nickname(nickname)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.checkNicknameDuplication(request.getNickname()))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }

    @DisplayName("회원가입을 진행할 때 중복된 이메일이 존재하면 예외가 발생한다.")
    @Test
    void createUserWithDuplicationUsername() {
        // given
        final String username = "test@email.com";
        User user = createUser(username, "password", "nickname1");
        userRepository.save(user);

        UserCreateRequest request = UserCreateRequest.builder()
                .username(username)
                .password("password")
                .nickname("nickname2")
                .department("department")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @DisplayName("이메일 중복검사를 할 때 이미 존재하는 이메일이면 예외가 발생한다.")
    @Test
    void checkUsernameWithDuplicationUsername() {
        // given
        final String username = "test@email.com";
        User user = createUser(username, "password", "nickname1");
        userRepository.save(user);

        UsernameRequest request = UsernameRequest.builder()
                .username(username)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.checkUsernameDuplication(request.getUsername()))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    private User createUser(final String username, final String password, final String nickname) {
        return User.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .department("department")
                .nickname(nickname)
                .build();
    }

    private User joinUser(final String username) {
        return User.builder()
                .username(username)
                .role(Role.GUEST)
                .build();
    }
}