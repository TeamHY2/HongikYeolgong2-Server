package com.hongik.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.dto.user.request.NicknameRequest;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.request.UsernameRequest;
import com.hongik.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
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
    @CsvSource({"닉 네 임", "닉네임!!", "닉네임8자넘었습니다", "닉", "!!!!", "......"})
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
                .andExpect(jsonPath("$.message").value("띄어쓰기, 특수문자는 불가능하고, 2~8자까지 허용합니다."));
    }

    @DisplayName("닉네임 중복검사")
    @Test
    void duplicateNickname() throws Exception {
        // given
        NicknameRequest request = NicknameRequest.builder()
                .nickname("nickname")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/duplicate-nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("닉네임 중복검사할 때 닉네임은 필수값이다.")
    @Test
    void duplicateNicknameWithoutNickname() throws Exception {
        // given
        NicknameRequest request = NicknameRequest.builder()
//                .nickname("nickname")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/duplicate-nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("닉네임은 필수입니다."));
    }

    @DisplayName("닉네임 중복검사할 때 닉네임은 길이 2~8자, 띄어쓰기와 특수문자는 허용하지 않는다.")
    @CsvSource({"닉 네 임", "닉네임!!", "닉네임8자넘었습니다", "닉", "!!!!", "......"})
    @ParameterizedTest
    void duplicateNicknameWithInvalidNickname(final String nickname) throws Exception {
        // given
        NicknameRequest request = NicknameRequest.builder()
                .nickname(nickname)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/user/duplicate-nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("띄어쓰기, 특수문자는 불가능하고, 2~8자까지 허용합니다."));
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
}