package com.hongik.controller.wisesaying;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.wisesaying.request.WiseSayingCreateRequest;
import com.hongik.dto.wisesaying.response.WiseSayingResponse;
import com.hongik.service.wisesaying.WiseSayingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "WiseSaying Controller - 명언 컨트롤러", description = "명언 저장, 조회 기능입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/wise-saying")
@RestController
public class WiseSayingController {

    private final WiseSayingService wiseSayingService;

    @Operation(summary = "명언들 저장", description = "명언을 여러개 한번에 저장합니다. JSON 형식입니다.")
    @PostMapping
    public ApiResponse<List<WiseSayingResponse>> createWiseSaying(@RequestBody List<WiseSayingCreateRequest> request) {
        return ApiResponse.ok(wiseSayingService.createWiseSaying(request));
    }

    @Operation(summary = "명언 조회", description = "명언을 하나씩 조회합니다. 순서는 서버에서 관리합니다.")
    @GetMapping
    public ApiResponse<WiseSayingResponse> getWiseSaying() {
        LocalDate now = LocalDate.now();
        return ApiResponse.ok(wiseSayingService.getWiseSaying(now));
    }
}
