package com.hongik.service.user;

import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.SocialPlatform;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMergeTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private StudySessionRepository studySessionRepository;

	@InjectMocks
	private UserMergeService userMergeService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@DisplayName("중복된 sub 값을 가진 유저 2명을 병합한다.")
	@Test
	void mergeUsersBySub_success() {
		// given
		String duplicateSub = "apple-sub-123";
		User olderUser = createUser(1L, duplicateSub, LocalDateTime.now().minusDays(1));
		User newerUser = createUser(2L, duplicateSub, LocalDateTime.now());

		when(userRepository.findBySocialPlatformAndSub(SocialPlatform.APPLE, duplicateSub))
				.thenReturn(new ArrayList<>(List.of(newerUser, olderUser)));


		// when
		Map<Long, Long> result = userMergeService.mergeUsersBySub(duplicateSub);

		// then
		assertThat(result).containsEntry(newerUser.getId(), olderUser.getId());
		verify(studySessionRepository).updateUserId(olderUser.getId(), newerUser.getId());
		verify(userRepository).delete(olderUser);
	}

	@DisplayName("중복된 sub 유저가 2명이 아닌 경우 예외를 발생시킨다.")
	@Test
	void mergeUsersBySub_fail_notExactlyTwo() {
		// given
		String duplicateSub = "apple-sub-456";
		User onlyUser = createUser(1L, duplicateSub, LocalDateTime.now());

		when(userRepository.findBySocialPlatformAndSub(SocialPlatform.APPLE, duplicateSub))
				.thenReturn(List.of(onlyUser));

		// when // then
		assertThatThrownBy(() -> userMergeService.mergeUsersBySub(duplicateSub))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.NOT_DUPLICATE_USER.getMessage());
	}

	@DisplayName("여러 중복된 sub 값을 가진 유저들을 일괄 병합한다.")
	@Test
	void mergeAllAppleUserWithDuplicateSub_success() {
		// given
		String sub1 = "sub1";
		String sub2 = "sub2";

		User sub1_user1 = createUser(1L, sub1, LocalDateTime.now().minusDays(2));
		User sub1_user2 = createUser(2L, sub1, LocalDateTime.now());

		User sub2_user1 = createUser(3L, sub2, LocalDateTime.now().minusDays(3));
		User sub2_user2 = createUser(4L, sub2, LocalDateTime.now().minusHours(1));

		when(userRepository.findDuplicateSubsBySocialPlatform(SocialPlatform.APPLE))
				.thenReturn(new ArrayList<>(List.of(sub1, sub2)));

		when(userRepository.findBySocialPlatformAndSub(SocialPlatform.APPLE, sub1))
				.thenReturn(new ArrayList<>(List.of(sub1_user2, sub1_user1)));

		when(userRepository.findBySocialPlatformAndSub(SocialPlatform.APPLE, sub2))
				.thenReturn(new ArrayList<>(List.of(sub2_user2, sub2_user1)));

		// when
		Map<Long, Long> result = userMergeService.mergeAllAppleUserWithDuplicateSub();

		// then
		assertThat(result).hasSize(2);
		assertThat(result).containsEntry(2L, 1L); // sub1: latest is 2
		assertThat(result).containsEntry(4L, 3L); // sub2: latest is 4

		verify(userRepository, times(2)).delete(any());
		verify(studySessionRepository, times(2)).updateUserId(anyLong(), anyLong());
	}

	private User createUser(Long id, String sub, LocalDateTime createdAt) {
		User user = mock(User.class);
		when(user.getId()).thenReturn(id);
		when(user.getSub()).thenReturn(sub);
		when(user.getCreatedAt()).thenReturn(createdAt);
		return user;
	}
}
