package com.hongik.controller.study;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.study.request.EndStudySessionRequest;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.request.StudySessionCreateRequest2;
import com.hongik.dto.study.response.*;
import com.hongik.service.study.StudySessionService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.hongik.exception.ErrorCode.*;

@Tag(name = "Study Session Controller - 스터디 세션 컨트롤러", description = "열람실을 이용한 시간을 등록하고, 나의 공부 시간, 공부 횟수를 조회합니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/study/session")
@RestController
public class StudySessionController {

    private final StudySessionService studySessionService;

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "열람실 이용 시작", description = "열람실 이용을 시작합니다. 시작 시간을 요청값에 담아주세요.")
    @PostMapping("/start")
    public ApiResponse<StudySessionStartResponse> createStudy(@Valid @RequestBody StudySessionCreateRequest2 request,
                                                         Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.createStudy(request, userId));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_STUDY_SESSION})
    @Operation(summary = "열람실 이용 종료", description = "열람실 이용을 종료합니다. studySessionId와 종료 시간을 요청값에 담아주세요.")
    @PatchMapping("/end")
    public ApiResponse<EndStudySessionResponse> updateStudy(@Valid @RequestBody EndStudySessionRequest request) {
        return ApiResponse.ok(studySessionService.updateStudy(request));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE})
    @Operation(summary = "열람실 이용 중인 회원 조회 (포커스 모드 회원 조회)", description = "열람실 이용 중인 회원(포커스 모드 회원 조회)을 조회합니다. 열람실 이용 시간이 긴 순서로 회원을 정렬합니다. ")
    @GetMapping
    public ApiResponse<List<StudyingUserResponse>> getStudyingUsers() {
        return ApiResponse.ok(studySessionService.getStudyingUsers());
    }
}
