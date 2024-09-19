package com.hongik.controller.study;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.StudyCountResponse;
import com.hongik.dto.study.response.StudyDurationResponse;
import com.hongik.dto.study.response.StudySessionResponse;
import com.hongik.service.study.StudySessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Study Controller - 스터디세션 컨트롤러", description = "열람실을 이용한 시간을 등록하고, 나의 공부 시간, 공부 횟수를 조회합니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class StudySessionController {

    private final StudySessionService studySessionService;

    @Operation(summary = "열람실 이용 종료", description = "열람실 이용을 종료합니다. 시작 시간과 종료 시간을 요청값에 담아주세요.")
    @PostMapping
    public ApiResponse<StudySessionResponse> createStudy(@Valid @RequestBody StudySessionCreateRequest request,
                                                         Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.createStudy(request, userId));
    }

    @Operation(summary = "월간, 투데이 공부 시간 조회", description = "해당 년, 월, 일에 대한 데이터를 넣어주세요. <br> Minute을 기준으로 제공합니다.")
    @GetMapping("/duration")
    public ApiResponse<StudyDurationResponse> getStudyDuration(@RequestParam int year,
                                                               @RequestParam int month,
                                                               @RequestParam int day,
                                                               Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyDuration(year, month, day, userId));
    }

    @Operation(summary = "특정 날짜 공부 횟수 조회", description = "해당 년, 월에 대한 데이터를 넣어주세요.")
    @GetMapping("/count")
    public ApiResponse<List<StudyCountResponse>> getStudyCount(@RequestParam int year,
                                                              @RequestParam int month,
                                                              Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyCount(year, month, userId));
    }

    @Operation(summary = "모든 날짜 공부 횟수 조회", description = "모든 날짜에 대한 공부 횟수를 조회합니다.")
    @GetMapping("/count-all")
    public ApiResponse<List<StudyCountResponse>> getStudyCountAll(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyCountAll(userId));
    }
}