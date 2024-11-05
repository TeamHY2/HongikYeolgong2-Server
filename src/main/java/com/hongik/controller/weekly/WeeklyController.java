package com.hongik.controller.weekly;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.weekly.response.WeeklyOneResponse;
import com.hongik.dto.weekly.response.WeeklyResponse;
import com.hongik.service.weekly.WeeklyService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.hongik.exception.ErrorCode.*;
import static com.hongik.exception.ErrorCode.NOT_FOUND_USER;

@Tag(name = "Weekly Controller - 위클리 컨트롤러", description = "연도에 대한 데이터를 저장하고 조회합니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/week-field")
@RestController
public class WeeklyController {

    private final WeeklyService weeklyService;

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, NOT_FOUND_USER, NOT_AUTHORITY})
    @Operation(summary = "20xx년에 대한 1월 1주차 1월 2주차.. 등 데이터 저장", description = "ADMIN만 가능하며, Param으로 20xx 값을 넣으면 됩니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<List<WeeklyResponse>> createWeekFields(@RequestParam int year) {
        List<WeeklyResponse> weekFields = weeklyService.createWeekFields(year);
        return ApiResponse.ok(weekFields);
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "(서버기준)20xx년에 대한 1월 1주차, 1월 2주차... 등 데이터 조회", description = "서버 시간 기준으로 20xx년에 대한 주차 데이터 조회합니다.")
    @GetMapping("/all")
    public ApiResponse<List<WeeklyResponse>> getWeeklyFields() {
        int year = LocalDate.now().getYear();
        return ApiResponse.ok(weeklyService.getWeeklyFields(year));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "LocalDate를 사용하여 몇 월 몇 주차, YearWeek 데이터 조회", description = "2024-10-25 기준 202443 데이터를 받습니다.")
    @GetMapping
    public ApiResponse<WeeklyOneResponse> getWeekly(@RequestParam LocalDate date) {
        return ApiResponse.ok(weeklyService.getWeekly(date));
    }
}
