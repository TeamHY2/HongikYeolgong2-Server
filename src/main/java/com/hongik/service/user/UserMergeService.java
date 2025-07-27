package com.hongik.service.user;

import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.SocialPlatform;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMergeService {
	private final UserRepository userRepository;
	private final StudySessionRepository studySessionRepository;

	@Transactional
	public Map<Long, Long> mergeAllAppleUserWithDuplicateSub(){
		// socialPlatform = APPLE 이면서 sub가 중복된 sub 목록 조회
		Map<Long, Long> mergeUsers = new HashMap<>();
		List<String> duplicateSubs = userRepository.findDuplicateSubsBySocialPlatform(SocialPlatform.APPLE);

		for (String sub : duplicateSubs) {
			Map<Long, Long> mergeUser = mergeUsersBySub(sub);
			mergeUsers.putAll(mergeUser);
		}

		return mergeUsers;
	}

	@Transactional
	public Map<Long, Long> mergeUsersBySub(String sub) {
		Map<Long, Long> mergeUser = new HashMap<>();
		List<User> users = userRepository.findBySocialPlatformAndSub(SocialPlatform.APPLE, sub);

		if (users.size() != 2) {
			throw new AppException(ErrorCode.NOT_DUPLICATE_USER, ErrorCode.NOT_DUPLICATE_USER.getMessage());
		}

		// 최신 가입자 선정 (가입일 기준 내림차순)
		users.sort(Comparator.comparing(User::getCreatedAt).reversed());
		User latestUser = users.get(0);
		User duplicateUser = users.get(1);

		Long latestUserId = latestUser.getId();
		Long duplicateUserId = duplicateUser.getId();

		// 외래키 참조 업데이트
		studySessionRepository.updateUserId(duplicateUserId, latestUserId);

		// 중복 사용자 삭제
		userRepository.delete(duplicateUser);

		// 결과 저장
		mergeUser.put(latestUserId, duplicateUserId);
		return mergeUser;
	}
}
