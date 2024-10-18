package com.hongik.controller.weekly;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.weekly.response.WeeklyResponse;
import com.hongik.service.weekly.WeeklyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Weekly Controller - 위클리 컨트롤러", description = "연도에 대한 데이터를 저장하고 조회합니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/week-field")
@RestController
public class WeeklyController {

    private final WeeklyService weeklyService;

    @Operation(summary = "20xx년에 대한 1월 1주차 1월 2주차.. 등 데이터 저장", description = "ADMIN만 가능하며, Param으로 20xx 값을 넣으면 됩니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<List<WeeklyResponse>> createWeekFields(@RequestParam int year) {
        List<WeeklyResponse> weekFields = weeklyService.createWeekFields(year);
        return ApiResponse.ok(weekFields);
    }

    @Operation(summary = "(서버기준)20xx년에 대한 1월 1주차, 1월 2주차... 등 데이터 조회", description = "서버 시간 기준으로 20xx년에 대한 주차 데이터 조회합니다.")
    @GetMapping
    public ApiResponse<List<WeeklyResponse>> getWeeklyFields() {
        int year = LocalDate.now().getYear();
        return ApiResponse.ok(weeklyService.getWeeklyFields(year));
    }
}
