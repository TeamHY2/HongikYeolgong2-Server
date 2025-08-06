package com.hongik.service.study;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.domain.weekly.Weekly;
import com.hongik.domain.weekly.WeeklyRepository;
import com.hongik.dto.study.request.EndStudySessionRequest;
import com.hongik.dto.study.request.StudySessionCreateRequest;
import com.hongik.dto.study.request.StudySessionCreateRequest2;
import com.hongik.dto.study.response.*;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class StudySessionService {

	private final StudySessionRepository studySessionRepository;

	private final UserRepository userRepository;

	@Transactional
	public StudySessionStartResponse createStudy(StudySessionCreateRequest2 request, final Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

		StudySession savedStudySession = studySessionRepository.save(request.toEntity(user, true));

		return StudySessionStartResponse.of(savedStudySession);
	}

	@Transactional
	public EndStudySessionResponse updateStudy(EndStudySessionRequest request) {
		StudySession studySession = studySessionRepository.findById(request.getStudySessionId())
				.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_STUDY_SESSION,
						ErrorCode.NOT_FOUND_STUDY_SESSION.getMessage()));

		studySession.updateStudy(request.getEndTime(), false);

		return EndStudySessionResponse.of(studySession);
	}

	public List<StudyingUserResponse> getStudyingUsers() {
		LocalDate today = LocalDate.now();
		LocalDateTime startOfDay = today.atStartOfDay();
		LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

		List<UserStudyDuration> results = studySessionRepository.getUserStudyDurationForDayAsSeconds(
				startOfDay,
				endOfDay);

		return results.stream()
				.map(result -> {
					int totalSeconds = result.getTotalSeconds();
					int hours = totalSeconds / 3600;
					int minutes = (totalSeconds % 3600) / 60;
					int seconds = totalSeconds % 60;

					boolean studyStatus = result.getStudyStatus() == 1;

					return StudyingUserResponse.of(
							result.getUserId(),
							result.getNickname(),
							LocalTime.of(hours, minutes, seconds),
							studyStatus
					);
				})
				.collect(Collectors.toList());
	}
}
