package com.hongik.controller.weekly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.service.weekly.WeeklyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WeeklyController.class)
@EnableMethodSecurity
class WeeklyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WeeklyService weeklyService;

    @DisplayName("원하는 연도에 대한 주차 데이터 저장")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createWeekFields() throws Exception {
        // given
        final int year = 2024;

        // when // then
        mockMvc.perform(
                        post("/api/v1/week-field").with(csrf())
                                .param("year", String.valueOf(year))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("원하는 연도에 대한 주차 데이터 저장할 때 ADMIN이 아니면 예외 발생")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void createWeekFieldsWithoutAdmin() throws Exception {
        // given
        final int year = 2024;

        // when // then
        mockMvc.perform(
                        post("/api/v1/week-field").with(csrf())
                                .param("year", String.valueOf(year))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}