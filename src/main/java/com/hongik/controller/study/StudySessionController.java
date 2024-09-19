package com.hongik.controller.study;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.StudyCountResponse;
import com.hongik.dto.study.response.StudyDurationResponse;
import com.hongik.dto.study.response.StudySessionResponse;
import com.hongik.service.study.StudySessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class StudySessionController {

    private final StudySessionService studySessionService;

    @PostMapping
    public ApiResponse<StudySessionResponse> createStudy(@Valid @RequestBody StudySessionCreateRequest request,
                                                         Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.createStudy(request, userId));
    }

    @GetMapping("/duration")
    public ApiResponse<StudyDurationResponse> getStudyDuration(@RequestParam int year,
                                                               @RequestParam int month,
                                                               @RequestParam int day,
                                                               Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyDuration(year, month, day, userId));
    }

    @GetMapping("/count")
    public ApiResponse<List<StudyCountResponse>> getStudyCount(@RequestParam int year,
                                                              @RequestParam int month,
                                                              Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyCount(year, month, userId));
    }

    @GetMapping("/count-all")
    public ApiResponse<List<StudyCountResponse>> getStudyCountAll(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(studySessionService.getStudyCountAll(userId));
    }
}
