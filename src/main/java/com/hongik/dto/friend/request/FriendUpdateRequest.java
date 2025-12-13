package com.hongik.dto.friend.request;

import com.hongik.domain.friend.FriendStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FriendUpdateRequest {
	private Long notificationId;
	private Long friendId;
	private Long senderId;
	private FriendStatus friendStatus;
}
