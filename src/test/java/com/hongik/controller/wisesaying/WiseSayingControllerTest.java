package com.hongik.controller.wisesaying;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.dto.wisesaying.request.WiseSayingCreateRequest;
import com.hongik.dto.wisesaying.response.WiseSayingResponse;
import com.hongik.service.wisesaying.WiseSayingService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WiseSayingController.class)
class WiseSayingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WiseSayingService wiseSayingService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
    }

    @DisplayName("명언 여러개 저장 JSON 형식, List.of 로 감싸야 한다.")
    @Test
    void createWiseSaying() throws Exception {
        // given
        List<WiseSayingResponse> result = List.of();

        WiseSayingCreateRequest request = WiseSayingCreateRequest.builder()
                .quote("말")
                .author("저자")
                .build();

        BDDMockito.given(wiseSayingService.createWiseSaying(List.of(request))).willReturn(result);

        // when // then
        mockMvc.perform(
                        post("/api/v1/wise-saying").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(List.of(request)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("서버에 저장된 명언을 조회합니다.")
    @Test
    void getWiseSaying() throws Exception {
        // given
        LocalDate now = LocalDate.now();
        WiseSayingResponse result = WiseSayingResponse.builder().build();

        BDDMockito.given(wiseSayingService.getWiseSaying(now)).willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/wise-saying").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}