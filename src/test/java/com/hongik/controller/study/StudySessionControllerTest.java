package com.hongik.controller.study;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.dto.study.request.EndStudySessionRequest;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.request.StudySessionCreateRequest2;
import com.hongik.dto.study.response.*;
import com.hongik.service.study.StudySessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @DisplayName("열람실 이용 시작 - 시작 시간 기록")
    @Test
    void createStudy() throws Exception {
        // given
        StudySessionCreateRequest2 request = StudySessionCreateRequest2.builder()
                .startTime(LocalDateTime.now())
                .build();

        // when // then
        mockMvc.perform(post("/api/v1/study/start").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("열람실 이용 종료 - 종료 시간 기록")
    @Test
    void updateStudy() throws Exception {
        // given
        EndStudySessionRequest request = EndStudySessionRequest.builder()
                .studySessionId(1L)
                .endTime(LocalDateTime.now().plusMinutes(1))
                .build();

        // when // then
        mockMvc.perform(patch("/api/v1/study/end").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("열람실 이용 시작 - 시작 시간이 누락되면 예외 발생")
    @Test
    void createStudyWithoutStartTime() throws Exception {
        // given
        StudySessionCreateRequest2 request = StudySessionCreateRequest2.builder()
                // .startTime(LocalDateTime.now())
                .build();

        // when // then
        mockMvc.perform(post("/api/v1/study/start").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("시작 시간은 필수입니다."));
    }

    @DisplayName("열람실 이용 종료 - 종료 시간이 누락되면 예외 발생")
    @Test
    void updateStudyWithoutEndTime() throws Exception {
        // given
        EndStudySessionRequest request = EndStudySessionRequest.builder()
                .studySessionId(1L)
                // .endTime(LocalDateTime.now().plusMinutes(1))
                .build();

        // when // then
        mockMvc.perform(patch("/api/v1/study/end").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("끝나는 시간은 필수입니다."));
    }

    @DisplayName("열람실 이용 종료 - 세션 ID 누락 시 예외 발생")
    @Test
    void updateStudyWithoutSessionId() throws Exception {
        // given
        EndStudySessionRequest request = EndStudySessionRequest.builder()
                // .studySessionId(1L)
                .endTime(LocalDateTime.now().plusMinutes(1))
                .build();

        // when // then
        mockMvc.perform(patch("/api/v1/study/end").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("studySessionId는 필수입니다."));
    }

    @DisplayName("열람실 이용 중인 회원 조회")
    @Test
    void getStudyingUsers() throws Exception {
        // given
        List<StudyingUserResponse> response = List.of(
                StudyingUserResponse.builder()
                        .userId(1L)
                        .nickname("김철수")
                        .build(),
                StudyingUserResponse.builder()
                        .userId(2L)
                        .nickname("이영희")
                        .build()
        );

        BDDMockito.given(studySessionService.getStudyingUsers())
                .willReturn(response);

        // when // then
        mockMvc.perform(get("/api/v1/study").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].userId").value(1L))
                .andExpect(jsonPath("$.data[0].nickname").value("김철수"));
    }

    @DisplayName("특정 날짜에 대한 공부 시간 조회")
    @Test
    void getStudyDuration() throws Exception {
        // given
        StudyDurationResponse result = StudyDurationResponse.builder().build();
        LocalDate now = LocalDate.of(2024, 10, 1);

        BDDMockito.given(studySessionService.getStudyDuration(now, 1L))
                .willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/study/duration").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("특정 연도와 월에 대한 공부 횟수 조회")
    @Test
    void getStudyCount() throws Exception {
        // given
        List<StudyCountLocalDateResponse> result = List.of();

        BDDMockito.given(studySessionService.getStudyCount(2024, 9, 1L))
                .willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/study/count").with(csrf())
                                .param("year", "2024")
                                .param("month", "9")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("현재 날짜 기준으로 주단위 월, 일, 공부 횟수를 가져옵니다.")
    @Test
    void getStudyCountOfWeek() throws Exception {
        // given
        List<StudyCountResponse> result = List.of();

        BDDMockito.given(studySessionService.getStudyCountOfWeek(LocalDate.now(), 1L))
                .willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/study/week").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("yearWeek를 파라미터로 해당 주의 랭킹을 구합니다.")
    @Test
    void getStudyDurationRanking() throws Exception {
        // given
        final int yearWeek = 202404;
        WeeklyRankingResponse result = WeeklyRankingResponse.builder().build();

        BDDMockito.given(studySessionService.getStudyDurationRanking(yearWeek))
                .willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/study/ranking").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("yearWeek", String.valueOf(yearWeek))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
