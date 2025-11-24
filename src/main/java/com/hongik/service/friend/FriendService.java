package com.hongik.service.friend;

import com.hongik.domain.friend.DateType;
import com.hongik.domain.friend.Friend;
import com.hongik.domain.friend.FriendRepository;
import com.hongik.domain.friend.FriendStatus;
import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.friend.request.FriendCreateRequest;
import com.hongik.dto.friend.request.FriendUpdateRequest;
import com.hongik.dto.friend.response.FriendCreateResponse;
import com.hongik.dto.friend.response.FriendSearchResponse;
import com.hongik.dto.friend.response.FriendStudyResponse;
import com.hongik.dto.friend.response.FriendUpdateResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

	@Transactional
	public FriendCreateResponse createFriend(Long userId, FriendCreateRequest request) {
		User findSender = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
				ErrorCode.NOT_FOUND_USER.getMessage()));
		User findReceiver = userRepository.findById(request.getReceiverId())
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
						ErrorCode.NOT_FOUND_USER.getMessage()));

		Friend findFriend = friendRepository.findBySenderAndReceiver(findSender, findReceiver);

		if (findFriend != null) {
			if (findFriend.getFriendStatus() == FriendStatus.PENDING) {
				throw new AppException(ErrorCode.FRIEND_ALREADY_PENDING, ErrorCode.FRIEND_ALREADY_PENDING.getMessage());
			} else if (findFriend.getFriendStatus() == FriendStatus.ACCEPTED) {
				throw new AppException(ErrorCode.FRIEND_ALREADY_ACCEPTED,
						ErrorCode.FRIEND_ALREADY_ACCEPTED.getMessage());
			}

			findFriend.updateFriend(findSender, findReceiver, FriendStatus.PENDING);
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

		findFriend.updateRequest(request.getFriendStatus());

		return FriendUpdateResponse.builder()
				.senderId(findFriend.getSender().getId())
				.friendStatus(findFriend.getFriendStatus())
				.build();
	}

	public List<FriendSearchResponse> getFriend(Long userId, String nickname) {
		List<User> searchFriends = userRepository.findAllByNicknameContains(nickname);

		if (searchFriends.isEmpty()) {
			throw new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage());
		}

		return searchFriends.stream()
				.map(friend -> {
					FriendStatus status = friendRepository.findFriendRelation(userId, friend.getId())
							.map(Friend::getFriendStatus)
							.orElse(FriendStatus.NONE);

					return FriendSearchResponse.builder()
							.userId(friend.getId())
							.nickname(friend.getNickname())
							.friendStatus(status)
							.build();
				})
				.collect(Collectors.toList());
	}

	@Transactional
	public List<FriendStudyResponse> getFriendStudy(Long userId, DateType dateType) {
		List<Friend> findFriends = friendRepository.findFriend(userId);

		if (findFriends.isEmpty()) {
			throw new AppException(ErrorCode.NOT_FOUND_FRIEND,ErrorCode.NOT_FOUND_FRIEND.getMessage());
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start;
		LocalDateTime end;

		if (dateType == DateType.DAILY) {
			start = now.toLocalDate().atStartOfDay();                   // 00:00
			end = now.toLocalDate().atTime(23, 59, 59, 999_999_999);
		} else { // MONTH
			start = now.withDayOfMonth(1).toLocalDate().atStartOfDay();                       // 이번 달 1일 00:00
			end = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
					.toLocalDate()
					.atTime(23, 59, 59, 999_999_999);
		}

		return findFriends.stream()
				.map(findFriend -> {
					Long friendId= findFriend.getOtherUserId(userId);

					User friend = userRepository.findById(friendId).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

					List<StudySession> studyTime = studySessionRepository
							.findFriendStudyTime(friend.getId(), start, end);

					// 총 공부 시간 계산
					Duration totalDuration = studyTime.stream()
							.map(s -> Duration.between(s.getStartTime(), s.getEndTime()))
							.reduce(Duration.ZERO, Duration::plus);

					long totalHours = totalDuration.toHours();              // 총 시간
					long totalMinutes = totalDuration.toMinutes() % 60;    // 나머지 분
					long totalSeconds = totalDuration.getSeconds() % 60;   // 나머지 초
					String totalTime = totalHours + ":" + totalMinutes + ":" + totalSeconds;

					return FriendStudyResponse.builder()
							.userId(userId)
							.friendId(friend.getId())
							.friendNickname(friend.getNickname())
							.studyTime(totalTime)
							.build();
				})
				.collect(Collectors.toList());
	}
}
