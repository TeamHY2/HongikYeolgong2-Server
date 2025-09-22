package com.hongik.controller.user;

import com.hongik.dto.ApiResponse;
import com.hongik.service.user.UserMergeService;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserMergeController {
	private final UserMergeService userMergeService;

	@Hidden
	@PostMapping("/merge")
	public ApiResponse<Map<Long, Long>> mergeAppleUserWithDuplicateSub() {
		return ApiResponse.ok(userMergeService.mergeAllAppleUserWithDuplicateSub());
	}
}
