package com.hongik.controller.auth;

import com.hongik.dto.ApiResponse;
import com.hongik.service.auth.apple.AppleMigrationService;
import io.swagger.v3.oas.annotations.Hidden;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AppleMigrationController {

	private final AppleMigrationService appleMigrationService;

	@Hidden
	@PostMapping("/migration")
	public ApiResponse<Void> migrateAppleUsers() throws IOException {
		appleMigrationService.migrateAllUsers();
		return ApiResponse.ok(null);
	}
}
