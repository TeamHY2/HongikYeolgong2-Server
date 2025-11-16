package com.hongik.controller.friend;

import static com.hongik.exception.ErrorCode.FRIEND_ALREADY_ACCEPTED;
import static com.hongik.exception.ErrorCode.FRIEND_ALREADY_PENDING;
import static com.hongik.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.hongik.exception.ErrorCode.INVALID_JWT_EXCEPTION;
import static com.hongik.exception.ErrorCode.NOT_FOUND_FRIEND_REQUEST;
import static com.hongik.exception.ErrorCode.REGISTRATION_INCOMPLETE;

import com.hongik.domain.user.User;
import com.hongik.dto.ApiResponse;
import com.hongik.dto.friend.request.FriendCreateRequest;
import com.hongik.dto.friend.request.FriendUpdateRequest;
import com.hongik.dto.friend.response.FriendCreateResponse;
import com.hongik.dto.friend.response.FriendUpdateResponse;
import com.hongik.service.friend.FriendService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Friend Controller - 친구 컨트롤러", description = "친구 요청/수락/거절/삭제와 친구 열람실 이용 시간을 조회합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/friends")
public class FriendController {
	private final FriendService friendService;

	@PostMapping()
	@Operation(summary = "친구 요청 전송 API", description = "친구 요청 전송 API. 요청 받는 사람의 id를 receiverId로 요청값에 담아주세요.")
	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE,
			FRIEND_ALREADY_PENDING, FRIEND_ALREADY_ACCEPTED})
	public ApiResponse<FriendCreateResponse> createFriend(Authentication authentication,
														  @RequestBody FriendCreateRequest request) {
		return ApiResponse.ok(friendService.createFriend(Long.parseLong(authentication.getName()), request));
	}

	@PatchMapping()
	@Operation(summary = "친구 요청 수락/거절 API", description = "친구 요청 수락/거절 API. senderId와 friendStatus를 요청 값에 담아주세요.")
	@ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE,
			NOT_FOUND_FRIEND_REQUEST})
	public ApiResponse<FriendUpdateResponse> updateFriend(Authentication authentication,
														  @RequestBody FriendUpdateRequest request) {
		return ApiResponse.ok(friendService.updateFriend(Long.parseLong(authentication.getName()), request));
	}


}
