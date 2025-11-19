package com.hongik.service.friend;

import com.hongik.domain.friend.DateType;
import com.hongik.domain.friend.Friend;
import com.hongik.domain.friend.FriendRepository;
import com.hongik.domain.friend.FriendStatus;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.friend.request.FriendCreateRequest;
import com.hongik.dto.friend.request.FriendUpdateRequest;
import com.hongik.dto.friend.response.FriendCreateResponse;
import com.hongik.dto.friend.response.FriendSearchResponse;
import com.hongik.dto.friend.response.FriendStudySessionResponse;
import com.hongik.dto.friend.response.FriendUpdateResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
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

	@Transactional
	public FriendCreateResponse createFriend(Long userId, FriendCreateRequest request) {
		User findSender = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
				ErrorCode.NOT_FOUND_USER.getMessage()));
		User findReceiver = userRepository.findById(request.getReceiverId())
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
						ErrorCode.NOT_FOUND_USER.getMessage()));

		Friend findFriend = friendRepository.findBySenderAndReceiver(findSender, findReceiver);
		if (findFriend.getFriendStatus() == FriendStatus.PENDING) {
			throw new AppException(ErrorCode.FRIEND_ALREADY_PENDING, ErrorCode.FRIEND_ALREADY_PENDING.getMessage());
		} else if (findFriend.getFriendStatus() == FriendStatus.ACCEPTED) {
			throw new AppException(ErrorCode.FRIEND_ALREADY_ACCEPTED, ErrorCode.FRIEND_ALREADY_ACCEPTED.getMessage());
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
}
