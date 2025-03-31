package com.hongik.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.dto.user.request.*;
import com.hongik.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
    }

    @DisplayName("회원가입")
    @Test
    void createUser() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("email@email.com")
                .password("password")
                .nickname("nickname")
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입할 때 이메일은 필수값이다.")
    @Test
    void createUserWithoutUsername() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
//                .username("email@email.com")
                .password("password")
                .nickname("nickname")
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("회원가입할 때 비밀번호는 필수값이다.")
    @Test
    void createUserWithoutPassword() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("email@email.com")
//                .password("password")
                .nickname("nickname")
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."));
    }

    @DisplayName("회원가입할 때 닉네임은 필수값이다.")
    @Test
    void createUserWithoutNickname() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("email@email.com")
                .password("password")
//                .nickname("nickname")
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("닉네임은 필수입니다."));
    }

    @DisplayName("회원가입할 때 학과는 필수값이다.")
    @Test
    void createUserWithoutDepartment() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("email@email.com")
                .password("password")
                .nickname("nickname")
//                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("학과는 필수입니다."));
    }

    @DisplayName("회원가입할 때 닉네임은 길이 2~8자, 띄어쓰기와 특수문자는 허용하지 않는다.")
    @CsvSource({"닉 네 임", "닉네임!!", "닉네임8자넘었습니다", "닉", "!!!!", "......", "안녕2"})
    @ParameterizedTest
    void createUserWithInvalidNickname(final String nickname) throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("email@email.com")
                .password("password")
                .nickname(nickname)
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("띄어쓰기, 숫자, 특수문자는 불가능하고, 2~8자까지 허용합니다."));
    }

    @DisplayName("닉네임 중복검사")
    @Test
    void duplicateNickname() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/duplicate-nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("nickname", "nickname")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("닉네임 중복검사할 때 닉네임은 필수값이다.")
    @Test
    void duplicateNicknameWithoutNickname() throws Exception {
        // NotBlank가 테스트 코드에서 안 먹는다.

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/duplicate-nickname").with(csrf())
                                .param("nickname", "")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @DisplayName("닉네임 중복검사할 때 닉네임은 길이 2~8자, 띄어쓰기와 특수문자는 허용하지 않는다.")
    @CsvSource({"닉 네 임", "닉네임!!", "닉네임8자넘었습니다", "닉", "!!!!", "......", "안녕1"})
    @ParameterizedTest
    void duplicateNicknameWithInvalidNickname(final String nickname) throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/duplicate-nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("nickname", nickname)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("띄어쓰기, 숫자, 특수문자는 불가능하고, 2~8자까지 허용합니다."));
    }

    @DisplayName("이메일 중복검사할 때 이메일은 필수값이다.")
    @Test
    void duplicateUsernameWithoutUsername() throws Exception {
        // given
        UsernameRequest request = UsernameRequest.builder()
//                .username("test@email.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/duplicate-username").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("이메일 중복검사")
    @Test
    void duplicateUsername() throws Exception {
        // given
        UsernameRequest request = UsernameRequest.builder()
                .username("test@email.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/duplicate-username").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입")
    @Test
    void joinUser() throws Exception {
        // given
        UserJoinRequest request = UserJoinRequest.builder()
                .nickname("nickname")
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/join").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입할 때 닉네임은 필수값이다.")
    @Test
    void joinUserWithoutNickname() throws Exception {
        // given
        UserJoinRequest request = UserJoinRequest.builder()
//                .nickname("nickname")
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/join").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("닉네임은 필수입니다."));
    }

    @DisplayName("회원가입할 때 학과는 필수값이다.")
    @Test
    void joinUserWithoutDepartment() throws Exception {
        // given
        UserJoinRequest request = UserJoinRequest.builder()
                .nickname("nickname")
//                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/join").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("학과는 필수입니다."));
    }

    @DisplayName("프로필을 수정할 때 닉네임은 필수값이다.")
    @Test
    void updateProfileWithoutNickname() throws Exception {
        // given
        UserProfileRequest request = UserProfileRequest.builder()
//                .nickname("nickname")
                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/user").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("닉네임은 필수입니다."));
    }

    @DisplayName("프로필을 수정할 때 학과는 필수값이다.")
    @Test
    void updateProfileWithoutDepartment() throws Exception {
        // given
        UserProfileRequest request = UserProfileRequest.builder()
                .nickname("nickname")
//                .department("department")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/user").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("학과는 필수입니다."));
    }

    @DisplayName("디바이스 토큰을 추가한다.")
    @Test
    void updateDeviceToken() throws Exception {
        // given
        UserDeviceTokenRequest request = UserDeviceTokenRequest.builder()
                .deviceToken("deviceToken")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/user/device-token").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("디바이스 토큰을 추가할 때, 디바이스 토큰 값은 필수다.")
    @Test
    void updateDeviceTokenWithoutDeviceToken() throws Exception {
        // given
        UserDeviceTokenRequest request = UserDeviceTokenRequest.builder()
//                .deviceToken("deviceToken")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/user/device-token").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("디바이스 토큰은 필수입니다."));
    }

    @DisplayName("디바이스 토큰을 조회한다.")
    @Test
    void getDeviceToken() throws Exception {
        // when // then
        mockMvc.perform(
                        get("/api/v1/user/device-token").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}