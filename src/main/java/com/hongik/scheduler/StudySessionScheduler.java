package com.hongik.scheduler;

import com.hongik.service.study.StudySessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudySessionScheduler {
	private final StudySessionService studySessionService;

	/**
	 * 1분마다 실행
	 */
	@Scheduled(fixedRate = 60_000)
	@Transactional
	public void autoCloseStudySession() {
		studySessionService.studySessionScheduler();
	}
}

