package com.hongik.controller.study;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.response.StudySessionResponse;
import com.hongik.service.study.StudySessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
