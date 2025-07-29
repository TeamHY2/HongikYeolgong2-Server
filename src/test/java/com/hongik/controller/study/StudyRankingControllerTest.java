package com.hongik.controller.study;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.dto.study.response.WeeklyRankingResponse;
import com.hongik.service.study.StudyRankingService;
import com.hongik.service.study.StudyRecordService;
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

@WebMvcTest(controllers = StudyRankingController.class)
public class StudyRankingControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StudyRankingService studyRankingService;

	@BeforeEach
	void setUp() {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
	}

	@DisplayName("yearWeek를 파라미터로 해당 주의 랭킹을 구합니다.")
	@Test
	void getStudyDurationRanking() throws Exception {
		// given
		final int yearWeek = 202404;
		WeeklyRankingResponse result = WeeklyRankingResponse.builder().build();

		BDDMockito.given(studyRankingService.getStudyDurationRanking(yearWeek))
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
