package com.hongik.controller.study;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hongik.dto.study.response.StudyCountLocalDateResponse;
import com.hongik.dto.study.response.StudyCountResponse;
import com.hongik.dto.study.response.StudyDurationResponse;
import com.hongik.service.study.StudyRecordService;
import java.time.LocalDate;
import java.util.List;
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

@WebMvcTest(controllers = StudyRecordController.class)
public class StudyRecordControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StudyRecordService studyRecordService;

	@BeforeEach
	void setUp() {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
	}

	@DisplayName("특정 날짜에 대한 공부 시간 조회")
	@Test
	void getStudyDuration() throws Exception {
		// given
		StudyDurationResponse result = StudyDurationResponse.builder().build();
		LocalDate now = LocalDate.of(2024, 10, 1);

		BDDMockito.given(studyRecordService.getStudyDuration(now, 1L))
				.willReturn(result);

		// when // then
		mockMvc.perform(
						get("/api/v1/study/record/duration").with(csrf())
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

		BDDMockito.given(studyRecordService.getStudyCount(2024, 9, 1L))
				.willReturn(result);

		// when // then
		mockMvc.perform(
						get("/api/v1/study/record/count").with(csrf())
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

		BDDMockito.given(studyRecordService.getStudyCountOfWeek(LocalDate.now(), 1L))
				.willReturn(result);

		// when // then
		mockMvc.perform(
						get("/api/v1/study/record/week").with(csrf())
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}
}
