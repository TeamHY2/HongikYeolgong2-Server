package com.hongik.dto.notification.response;

import com.hongik.domain.notification.NotificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class NotificationResponse {
	private Long notificationId;
	private NotificationType type;
	private String content;
	private String receivedAt;
	private Long friendId;
	private Long receiverId;
	private Long senderId;
	private String senderNickname;
}
