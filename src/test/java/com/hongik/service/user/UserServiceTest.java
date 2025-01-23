package com.hongik.service.user;

import com.hongik.discord.MessageEvent;
import com.hongik.discord.MessageService;
import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.user.request.*;
import com.hongik.dto.user.response.JoinResponse;
import com.hongik.dto.user.response.NicknameResponse;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.AppException;
import com.hongik.jwt.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.ApplicationEvents;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

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

        BDDMockito.given(jwtUtil.createAccessToken(any(), anyLong())).willReturn("ey");

        // when
        JoinResponse userResponse = userService.join(request, user.getId());

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "nickname", "department", "accessToken")
                .containsExactlyInAnyOrder("user@gmail.com", "nickname", "department", "ey");
    }

    @DisplayName("회원가입을 진행할 때 중복된 닉네임이 존재하면 예외가 발생한다.")
    @Test
    void createUserWithDuplicationNickname() {
        // given
        final String nickname = "nickname";
        User user = createUser("test@email.com", "password", nickname);
        userRepository.save(user);

        UserCreateRequest request = UserCreateRequest.builder()
                .username("test2@email.com")
                .password("password")
                .nickname(nickname)
                .department("department")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }

    @DisplayName("닉네임 중복검사를 할 때 이미 닉네임이 존재하면 true를 반환한다.")
    @Test
    void checkNicknameDuplication() {
        // given
        final String nickname = "nickname";
        User user = createUser("test@email.com", "password", nickname);
        userRepository.save(user);

        NicknameRequest request = NicknameRequest.builder()
                .nickname(nickname)
                .build();

        // when
        NicknameResponse nicknameResponse = userService.checkNicknameDuplication(request.getNickname());

        // then
        assertThat(nicknameResponse)
                .extracting("nickname", "isDuplicate")
                .containsExactlyInAnyOrder(nickname, true);
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

    @DisplayName("프로필(닉네임, 학과)을 수정한다.")
    @Test
    void updateProfile() {
        // given
        User user = createUser("user@email.com", "password", "nickname");
        userRepository.save(user);

        UserProfileRequest request = UserProfileRequest.builder()
                .nickname("수정된닉네임")
                .department("수정된학과")
                .build();

        // when
        UserResponse userResponse = userService.updateProfile(request, user.getId());

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "nickname", "department")
                .containsExactlyInAnyOrder("user@email.com", "수정된닉네임", "수정된학과");
    }

    @DisplayName("프로필(닉네임, 학과)을 수정할 때, 본인 기존 닉네임과 같은 닉네임인 경우 중복 예외가 발생하지 않는다.")
    @Test
    void updateProfileWithoutNickname() {
        // given
        User user = createUser("user@email.com", "password", "nickname");
        userRepository.save(user);

        UserProfileRequest request = UserProfileRequest.builder()
                .nickname("nickname")
                .department("수정된학과")
                .build();

        // when
        UserResponse userResponse = userService.updateProfile(request, user.getId());

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "nickname", "department")
                .containsExactlyInAnyOrder("user@email.com", "nickname", "수정된학과");
    }

    @DisplayName("프로필(닉네임, 학과)을 수정할 때, 다른 사람 닉네임과 같으면 닉네임 중복 예외가 발생한다.")
    @Test
    void updateProfileWithDuplicationNickname() {
        // given
        User user = createUser("test@email.com", "password", "다른사람닉네임");
        User me = createUser("user@email.com", "password", "닉네임2");
        userRepository.saveAll(List.of(user, me));

        UserProfileRequest request = UserProfileRequest.builder()
                .nickname("다른사람닉네임")
                .department("수정된학과")
                .build();


        // when // then
        assertThatThrownBy(() -> userService.updateProfile(request, me.getId()))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
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