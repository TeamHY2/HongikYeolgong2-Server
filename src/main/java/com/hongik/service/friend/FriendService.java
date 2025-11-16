package com.hongik.service.friend;

import com.hongik.domain.friend.Friend;
import com.hongik.domain.friend.FriendRepository;
import com.hongik.domain.friend.FriendStatus;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.friend.request.FriendCreateRequest;
import com.hongik.dto.friend.response.FriendCreateResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {
	private final FriendRepository friendRepository;
	private final UserRepository userRepository;

	public FriendCreateResponse createFriend(Long userId, FriendCreateRequest request) {
		User findSender = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
				ErrorCode.NOT_FOUND_USER.getMessage()));
		User findReceiver = userRepository.findById(request.getReceiverId())
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,
						ErrorCode.NOT_FOUND_USER.getMessage()));

		Friend findFriend = friendRepository.findBySenderAndReceiver(findSender, findReceiver);
		if (findFriend.getFriendStatus() == FriendStatus.PENDING) {
			throw new AppException(ErrorCode.FRIEND_ALREADY_APPENDING, ErrorCode.FRIEND_ALREADY_APPENDING.getMessage());
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
}
