package com.hongik.dto.friend.response;

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
public class FriendCreateResponse {
	private Long id;
	private Long receiverId;
	private FriendStatus friendStatus;
}
