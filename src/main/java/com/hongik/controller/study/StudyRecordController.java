package com.hongik.controller.study;

import static com.hongik.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.hongik.exception.ErrorCode.INVALID_JWT_EXCEPTION;
import static com.hongik.exception.ErrorCode.NOT_FOUND_USER;
import static com.hongik.exception.ErrorCode.REGISTRATION_INCOMPLETE;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.study.response.StudyCountLocalDateResponse;
import com.hongik.dto.study.response.StudyCountResponse;
import com.hongik.dto.study.response.StudyDurationResponse;
import com.hongik.service.study.StudyRecordService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Study Record Controller - 스터디 기록 컨트롤러", description = "나의 열람실 이용 시간, 열람실 이용 횟수를 조회합니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/study/record")
@RestController
public class StudyRecordController {

	private final StudyRecordService studyRecordService;

	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
	@Operation(summary = "(기록 화면 캘린더 공부 횟수와 함께 표시) 연간, 월간, 투데이 공부 시간 조회", description = "Param값이 없으면 서버 기준 현재 시간을 기준으로 날짜를 정합니다. <br> Minute을 제공합니다.")
	@GetMapping("/duration")
	public ApiResponse<StudyDurationResponse> getStudyDuration(Authentication authentication,
															   @RequestParam(required = false) LocalDate date) {
		Long userId = Long.parseLong(authentication.getName());
		if (date == null) {
			date = LocalDate.now();
		}
		return ApiResponse.ok(studyRecordService.getStudyDuration(date, userId));
	}

	@Hidden
	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
	@Operation(summary = "특정 날짜 공부 횟수 조회", description = "해당 년, 월에 대한 데이터를 넣어주세요.")
	@GetMapping("/count")
	public ApiResponse<List<StudyCountLocalDateResponse>> getStudyCount(@RequestParam int year,
																		@RequestParam int month,
																		Authentication authentication) {
		Long userId = Long.parseLong(authentication.getName());
		return ApiResponse.ok(studyRecordService.getStudyCount(year, month, userId));
	}

	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
	@Operation(summary = "(기록 화면 캘린더 공부 횟수) 모든 날짜 공부 횟수 조회", description = "모든 날짜에 대한 공부 횟수를 조회합니다.")
	@GetMapping("/count-all")
	public ApiResponse<List<StudyCountLocalDateResponse>> getStudyCountAll(Authentication authentication) {
		Long userId = Long.parseLong(authentication.getName());
		return ApiResponse.ok(studyRecordService.getStudyCountAll(userId));
	}

	/**
	 * 홈 화면 명언과 함께 나타낼 데이터다.
	 *
	 * @param authentication
	 * @return
	 */
	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
	@Operation(summary = "(홈 화면 명언과 함께 나타낼 데이터) 서버의 현재 날짜 기준으로 주단위 월, 일, 공부 횟수를 가져옵니다.", description = "한 주에 대한 공부 횟수를 조회합니다.")
	@GetMapping("/week")
	public ApiResponse<List<StudyCountResponse>> getStudyCountOfWeek(Authentication authentication) {
		Long userId = Long.parseLong(authentication.getName());
		LocalDate today = LocalDate.now();

		return ApiResponse.ok(studyRecordService.getStudyCountOfWeek(today, userId));
	}
}
