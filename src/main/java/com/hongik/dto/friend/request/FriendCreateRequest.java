package com.hongik.dto.friend.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FriendCreateRequest {
	private Long receiverId;
}
