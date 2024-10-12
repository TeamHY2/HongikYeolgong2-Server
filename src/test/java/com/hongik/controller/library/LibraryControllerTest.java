package com.hongik.controller.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.service.library.LibraryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableMethodSecurity
@WebMvcTest(controllers = LibraryController.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryService libraryService;

    @DisplayName("열람실 이용 시간 조회(4시간 or 6시간)")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void getLibraryHours() throws Exception {
        // when // then
        mockMvc.perform(
                        get("/api/v1/library").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("열람실 이용 시간 수정(4시간 or 6시간)")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateLibraryHours() throws Exception {
        // when // then
        mockMvc.perform(
                        put("/api/v1/library").with(csrf())
                                .param("libraryHours", String.valueOf(4))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("열람실 이용 시간 수정할 때, param값이 존재하지 않으면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateLibraryHoursWithoutParameter() throws Exception {
        // when // then
        mockMvc.perform(
                        put("/api/v1/library").with(csrf())
//                                .param("libraryHours", String.valueOf(4))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("열람실 이용 시간 수정할 때 ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void updateLibraryHoursWithoutAdmin() throws Exception {
        // when // then
        mockMvc.perform(
                        put("/api/v1/library").with(csrf())
                                .param("libraryHours", String.valueOf(4))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}