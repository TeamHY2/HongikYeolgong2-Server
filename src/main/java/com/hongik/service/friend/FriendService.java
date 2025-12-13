package com.hongik.service.friend;

import com.hongik.domain.friend.DateType;
import com.hongik.domain.friend.Friend;
import com.hongik.domain.friend.FriendRepository;
import com.hongik.domain.friend.FriendStatus;
import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.friend.request.FriendCancelRequest;
import com.hongik.dto.friend.request.FriendCreateRequest;
import com.hongik.dto.friend.request.FriendUpdateRequest;
import com.hongik.dto.friend.response.FriendCreateResponse;
import com.hongik.dto.friend.response.FriendSearchResponse;
import com.hongik.dto.friend.response.FriendStudyResponse;
import com.hongik.dto.friend.response.FriendUpdateResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import com.hongik.service.notification.NotificationService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {
	private final FriendRepository friendRepository;
	private final UserRepository userRepository;
	private final StudySessionRepository studySessionRepository;
	private final NotificationService notificationService;

	@Transactional
	public FriendCreateResponse createFriend(Long userId, FriendCreateRequest request) {
		User findSender = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
				ErrorCode.NOT_FOUND_USER.getMessage()));
		User findReceiver = userRepository.findById(request.getReceiverId())
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
						ErrorCode.NOT_FOUND_USER.getMessage()));

		Friend findFriend = friendRepository.findFriendRelation(findSender.getId(), findReceiver.getId()).orElse(null);

		if (findFriend != null) {
			if (findFriend.getFriendStatus() == FriendStatus.PENDING) {
				throw new AppException(ErrorCode.FRIEND_ALREADY_PENDING, ErrorCode.FRIEND_ALREADY_PENDING.getMessage());
			} else if (findFriend.getFriendStatus() == FriendStatus.ACCEPTED) {
				throw new AppException(ErrorCode.FRIEND_ALREADY_ACCEPTED,
						ErrorCode.FRIEND_ALREADY_ACCEPTED.getMessage());
			}

			findFriend.updateFriend(findSender, findReceiver, FriendStatus.PENDING);
			notificationService.createNotification(findFriend, findSender, findReceiver);

			return FriendCreateResponse.builder()
					.id(findFriend.getId())
					.receiverId(findFriend.getReceiver().getId())
					.friendStatus(findFriend.getFriendStatus())
					.build();
		}

		Friend friend = Friend.builder()
				.sender(findSender)
				.receiver(findReceiver)
				.friendStatus(FriendStatus.PENDING)
				.isDeleted(false)
				.build();
		friendRepository.save(friend);
		notificationService.createNotification(friend, findSender, findReceiver);

		return FriendCreateResponse.builder()
				.id(friend.getId())
				.receiverId(friend.getReceiver().getId())
				.friendStatus(friend.getFriendStatus())
				.build();
	}

	@Transactional
	public FriendUpdateResponse updateFriend(Long userId, FriendUpdateRequest request) {
		User findReceiver = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
				ErrorCode.NOT_FOUND_USER.getMessage()));
		User findSender = userRepository.findById(request.getSenderId())
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
						ErrorCode.NOT_FOUND_USER.getMessage()));
		Friend findFriend = friendRepository.findBySenderAndReceiverAndFriendStatus(findSender, findReceiver,
				FriendStatus.PENDING).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_FRIEND_REQUEST,
				ErrorCode.NOT_FOUND_FRIEND_REQUEST.getMessage()));

		findFriend.updateStatus(request.getFriendStatus());
		notificationService.updateNotification(request.getNotificationId(), request.getFriendStatus());

		return FriendUpdateResponse.builder()
				.senderId(findFriend.getSender().getId())
				.friendStatus(findFriend.getFriendStatus())
				.build();
	}

	@Transactional
	public void cancelFriend(Long userId, FriendCancelRequest request) {
		User findUser = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));
		User findCancelUser = userRepository.findById(request.getCancelUserId())
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

		Friend findFriend = friendRepository.findBySenderAndReceiver(findUser, findCancelUser);

		if (findFriend == null) {
			throw new AppException(ErrorCode.NOT_FOUND_FRIEND, ErrorCode.NOT_FOUND_FRIEND.getMessage());
		}

		findFriend.updateStatus(FriendStatus.NONE);
		notificationService.updateNotificationWithFriend(findFriend.getId(), FriendStatus.NONE);
	}

	@Transactional(readOnly = true)
	public List<FriendSearchResponse> getFriend(Long userId, String nickname) {
		List<User> searchFriends = userRepository.findAllByNicknameContainsAndIdNot(nickname, userId);

		if (searchFriends.isEmpty()) {
			return List.of();
		}

		return searchFriends.stream()
				.map(friend -> {
					// 현재 두 사용자 사이의 관계 조회
					Optional<Friend> friendRelation = friendRepository.findFriendRelation(userId, friend.getId());

					FriendStatus status = friendRelation
							.map(Friend::getFriendStatus)
							.orElse(FriendStatus.NONE);

					// 요청자/피요청자 여부 판단
					boolean isSender = friendRelation
							.map(r -> r.getSender().getId().equals(userId))
							.orElse(false);

					boolean canSendRequest = false;
					boolean canCancelRequest = false;

					// 상태별 처리
					switch (status) {
						case NONE, REJECTED -> {
							canSendRequest = true;
						}
						case PENDING -> {
							if (isSender) {
								canCancelRequest = true;
							}
						}
					}

					return FriendSearchResponse.builder()
							.userId(friend.getId())
							.nickname(friend.getNickname())
							.friendStatus(status)
							.canSendRequest(canSendRequest)
							.canCancelRequest(canCancelRequest)
							.build();
				})
				.collect(Collectors.toList());
	}

	@Transactional
	public List<FriendStudyResponse> getFriendStudy(Long userId, DateType dateType) {
		List<Friend> findFriends = friendRepository.findFriend(userId);

		if (findFriends.isEmpty()) {
			return List.of();
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start;
		LocalDateTime end;

		if (dateType == DateType.DAILY) {
			start = now.toLocalDate().atStartOfDay();
			end = now.toLocalDate().atTime(23, 59, 59, 999_999_999);
		} else { // MONTH
			start = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
			end = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
					.toLocalDate()
					.atTime(23, 59, 59, 999_999_999);
		}

		return findFriends.stream()
				.map(findFriend -> {
					Long friendId = findFriend.getOtherUserId(userId);

					User friend = userRepository.findById(friendId)
							.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
									ErrorCode.NOT_FOUND_USER.getMessage()));

					List<StudySession> studyTime = studySessionRepository
							.findFriendStudyTime(friend.getId(), start, end);

					// 총 공부 시간
					Duration totalDuration = studyTime.stream()
							.map(s -> Duration.between(s.getStartTime(), s.getEndTime()))
							.reduce(Duration.ZERO, Duration::plus);

					long totalHours = totalDuration.toHours();
					long totalMinutes = totalDuration.toMinutes() % 60;
					long totalSeconds = totalDuration.getSeconds() % 60;

					String totalTime = totalHours + ":" + totalMinutes + ":" + totalSeconds;

					return FriendStudyResponse.builder()
							.userId(userId)
							.friendId(friend.getId())
							.friendNickname(friend.getNickname())
							.studyTime(totalTime)
							.totalDuration(totalDuration)
							.build();
				})
				.sorted((a, b) -> b.getTotalDuration().compareTo(a.getTotalDuration()))
				.collect(Collectors.toList());
	}
}
