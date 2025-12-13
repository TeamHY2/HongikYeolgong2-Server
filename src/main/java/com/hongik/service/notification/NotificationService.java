package com.hongik.service.notification;

import com.hongik.domain.friend.Friend;
import com.hongik.domain.friend.FriendStatus;
import com.hongik.domain.notification.Notification;
import com.hongik.domain.notification.NotificationRepository;
import com.hongik.domain.notification.NotificationType;
import com.hongik.domain.user.User;
import com.hongik.dto.notification.response.NotificationResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationService {
	private final NotificationRepository notificationRepository;

	@Transactional
	public void createNotification(Friend friend, User sender, User receiver) {
		Notification notification = Notification.builder()
				.notificationType(NotificationType.REQUEST)
				.friend(friend)
				.receiver(receiver)
				.sender(sender)
				.content(sender.getNickname() + "님이 친구 요청을 보냈어요.")
				.isRead(false)
				.build();
		notificationRepository.save(notification);
	}

	@Transactional
	public void updateNotification(Long notificationId, FriendStatus friendStatus) {
		Notification notification = notificationRepository.findById(notificationId).orElseThrow(
				() -> new AppException(ErrorCode.NOT_FOUND_NOTIFICATION,
						ErrorCode.NOT_FOUND_NOTIFICATION.getMessage()));
		if (friendStatus.equals(FriendStatus.ACCEPTED) || friendStatus.equals(FriendStatus.REJECTED)
				|| friendStatus.equals(FriendStatus.NONE)) {
			notification.updateIsRead(true);
		}
	}

	@Transactional
	public void updateNotificationWithFriend(Long friendId, FriendStatus friendStatus) {
		Notification notification = notificationRepository.findTopByFriendIdOrderByCreatedAtDesc(friendId).orElseThrow(
				() -> new AppException(ErrorCode.NOT_FOUND_NOTIFICATION,
						ErrorCode.NOT_FOUND_NOTIFICATION.getMessage()));
		if (friendStatus.equals(FriendStatus.ACCEPTED) || friendStatus.equals(FriendStatus.REJECTED)
				|| friendStatus.equals(FriendStatus.NONE)) {
			notification.updateIsRead(true);
		}
	}

	@Transactional(readOnly = true)
	public List<NotificationResponse> getNotifications(Long userId) {
		List<Notification> findNotifications = notificationRepository.findByReceiverIdAndIsReadOrderByCreatedAtDesc(userId, false);

		if (findNotifications.isEmpty()) {
			return List.of();
		}

		return findNotifications.stream()
				.map(findNotification -> NotificationResponse.builder()
						.notificationId(findNotification.getId())
						.type(findNotification.getNotificationType())
						.content(findNotification.getContent())
						.receivedAt(calcReceivedAt(findNotification.getCreatedAt()))
						.friendId(findNotification.getFriend().getId())
						.receiverId(findNotification.getFriend().getReceiver().getId())
						.senderId(findNotification.getFriend().getSender().getId())
						.senderNickname(findNotification.getSender().getNickname())
						.build()
				).toList();
	}

	private String calcReceivedAt(LocalDateTime createdAt) {
		LocalDateTime now = LocalDateTime.now();

		Duration duration = Duration.between(createdAt, now);
		long minutes = duration.toMinutes();
		long hours = duration.toHours();
		long days = duration.toDays();

		if (days > 0) {
			return days + "일";
		} else if (hours > 0) {
			return hours + "시간";
		} else if (minutes > 0) {
			return minutes + "분";
		} else {
			return "방금";
		}
	}
}
