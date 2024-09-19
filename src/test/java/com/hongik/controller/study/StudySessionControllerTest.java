package com.hongik.controller.study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.service.study.StudySessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudySessionController.class)
class StudySessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudySessionService studySessionService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
    }

    @DisplayName("열람실 이용 종료, 시작 시간과 끝나는 시간 기록")
    @Test
    void createStudy() throws Exception {
        // given
        StudySessionCreateRequest request = StudySessionCreateRequest.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(1))
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/study").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("열람실 이용 종료시 시작 시간은 필수값이다.")
    @Test
    void createStudyWithoutStartTime() throws Exception {
        // given
        StudySessionCreateRequest request = StudySessionCreateRequest.builder()
//                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(1))
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/study").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("시작 시간은 필수입니다."));
    }

    @DisplayName("열람실 이용 종료시 끝나는 시간은 필수값이다.")
    @Test
    void createStudyWithoutEndTime() throws Exception {
        // given
        StudySessionCreateRequest request = StudySessionCreateRequest.builder()
                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusMinutes(1))
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/study").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("끝나는 시간은 필수입니다."));
    }
}