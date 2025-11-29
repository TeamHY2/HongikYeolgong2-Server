package com.hongik.controller.notification;

import static com.hongik.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.hongik.exception.ErrorCode.INVALID_JWT_EXCEPTION;
import static com.hongik.exception.ErrorCode.NOT_FOUND_FRIEND;
import static com.hongik.exception.ErrorCode.NOT_FOUND_NOTIFICATION;
import static com.hongik.exception.ErrorCode.NOT_FOUND_USER;
import static com.hongik.exception.ErrorCode.REGISTRATION_INCOMPLETE;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.notification.response.NotificationResponse;
import com.hongik.service.notification.NotificationService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification Controller - 알림 컨트롤러", description = "알림 목록을 조회한다.")
@RequiredArgsConstructor
@RequestMapping("/api/v2/notification")
@RestController
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping
	@Operation(summary = "알림 목록 조회 API", description = "알림 목록 조회 API. 친구 요청 받은 항목 목록으로 조회 가능합니다.")
	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_NOTIFICATION})
	public ApiResponse<List<NotificationResponse>> getNotifications(Authentication authentication){
		return ApiResponse.ok(notificationService.getNotifications(Long.parseLong(authentication.getName())));
	}
}
