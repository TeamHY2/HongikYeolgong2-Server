package com.hongik.controller.library;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.library.response.LibraryResponse;
import com.hongik.exception.ErrorCode;
import com.hongik.service.library.LibraryService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.hongik.exception.ErrorCode.*;

@Tag(name = "Library Controller - 열람실 좌석 이용시간 컨트롤러", description = "열람실 좌석 이용 시간을 조회(4 or 6)하고 이용 시간을 수정할 수 있습니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/library")
@RestController
public class LibraryController {

    private final LibraryService libraryService;

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "열람실 좌석 이용 가능한 시간 조회(4시간 or 6시간)", description = "열람실 좌석 이용 가능한 시간을 조회합니다. 일반 6시간, 시험기간 4시간")
    @GetMapping
    public ApiResponse<LibraryResponse> getLibraryHours(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(libraryService.getLibraryHours());
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, NOT_AUTHORITY})
    @Operation(summary = "열람실 좌석 이용 가능한 시간 수정(4시간 or 6시간)", description = "열람실 좌석 좌석 이용 가능한 시간을 수정합니다. 일반 6시간, 시험기간 4시간")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ApiResponse<LibraryResponse> updateLibraryHours(Authentication authentication,
                                                              @RequestParam int libraryHours) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(libraryService.updateLibraryHours(libraryHours));
    }

}
