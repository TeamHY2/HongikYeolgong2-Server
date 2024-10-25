package com.hongik.controller.study;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.*;
import com.hongik.service.study.StudySessionService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.hongik.exception.ErrorCode.*;

@Tag(name = "Study Controller - 스터디세션 컨트롤러", description = "열람실을 이용한 시간을 등록하고, 나의 공부 시간, 공부 횟수를 조회합니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class StudySessionController {

    private final StudySessionService studySessionService;

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "열람실 이용 종료", description = "열람실 이용을 종료합니다. 시작 시간과 종료 시간을 요청값에 담아주세요.")
    @PostMapping
    public ApiResponse<StudySessionResponse> createStudy(@Valid @RequestBody StudySessionCreateRequest request,
                                                         Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.createStudy(request, userId));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "(기록 화면 캘린더 공부 횟수와 함께 표시) 연간, 월간, 투데이, 학기 공부 시간 조회", description = "서버 기준 현재 시간을 기준으로 날짜를 정합니다. <br> Minute을 제공합니다.")
    @GetMapping("/duration")
    public ApiResponse<StudyDurationResponse> getStudyDuration(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        LocalDate now = LocalDate.now();
        return ApiResponse.ok(studySessionService.getStudyDuration(now, userId));
    }

    @Hidden
    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "특정 날짜 공부 횟수 조회", description = "해당 년, 월에 대한 데이터를 넣어주세요.")
    @GetMapping("/count")
    public ApiResponse<List<StudyCountResponse>> getStudyCount(@RequestParam int year,
                                                               @RequestParam int month,
                                                               Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyCount(year, month, userId));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "(기록 화면 캘린더 공부 횟수) 모든 날짜 공부 횟수 조회", description = "모든 날짜에 대한 공부 횟수를 조회합니다.")
    @GetMapping("/count-all")
    public ApiResponse<List<StudyCountResponse>> getStudyCountAll(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyCountAll(userId));
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

        return ApiResponse.ok(studySessionService.getStudyCountOfWeek(today, userId));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "(랭킹 화면) 주차별 랭킹을 가져온다.", description = "202401과 같은 데이터를 제공하면 랭킹을 구할 수 있습니다.")
    @GetMapping("/ranking")
    public ApiResponse<WeeklyRankingResponse> getStudyDurationRanking(Authentication authentication,
                                                                           @RequestParam int yearWeek) {
        Long userId = Long.parseLong(authentication.getName());

        return ApiResponse.ok(studySessionService.getStudyDurationRanking(yearWeek));
    }
}
