package com.hongik.dto.friend.request;

import com.hongik.domain.friend.FriendStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class FriendCancelRequest {
	private Long cancelUserId; // 취소할 사용자의 Id
}
