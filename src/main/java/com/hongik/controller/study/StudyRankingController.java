package com.hongik.controller.study;

import static com.hongik.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.hongik.exception.ErrorCode.INVALID_JWT_EXCEPTION;
import static com.hongik.exception.ErrorCode.NOT_FOUND_USER;
import static com.hongik.exception.ErrorCode.REGISTRATION_INCOMPLETE;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.study.response.WeeklyRankingResponse;
import com.hongik.service.study.StudyRankingService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study Ranking Controller - 스터디 랭킹 컨트롤러", description = "주차별 & 학과별 열람실 이용 시간 랭킹을 조회한다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/study/ranking")
@RestController
public class StudyRankingController {

	private final StudyRankingService studyRankingService;

	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
	@Operation(summary = "(랭킹 화면) 주차별 랭킹을 가져온다.", description = "202401과 같은 데이터를 제공하면 랭킹을 구할 수 있습니다.")
	@GetMapping
	public ApiResponse<WeeklyRankingResponse> getStudyDurationRanking(Authentication authentication,
																	  @RequestParam int yearWeek) {
		Long userId = Long.parseLong(authentication.getName());

		return ApiResponse.ok(studyRankingService.getStudyDurationRanking(yearWeek));
	}
}
