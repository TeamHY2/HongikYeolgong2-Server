package com.hongik.controller.weekly;

import com.hongik.dto.weekly.response.WeeklyResponse;
import com.hongik.service.weekly.WeeklyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/week-field")
@RestController
public class WeeklyController {

    private final WeeklyService weeklyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<List<WeeklyResponse>> createWeekFields(@RequestParam int year) {
        List<WeeklyResponse> weekFields = weeklyService.createWeekFields(year);
        return ResponseEntity.ok(weekFields);
    }

    @GetMapping
    public ResponseEntity<List<WeeklyResponse>> getWeeklyFields() {
        int year = LocalDate.now().getYear();
        return ResponseEntity.ok(weeklyService.getWeeklyFields(year));
    }
}
