package com.hongik.service.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import com.hongik.domain.user.UserRepository;
import com.hongik.dto.study.response.StudyCountLocalDateResponse;
import com.hongik.dto.study.response.StudyCountResponse;
import com.hongik.dto.study.response.StudyDurationResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudyRecordServiceTest {


	@Autowired
	private StudyRecordService studyRecordService;

	@Autowired
	private StudySessionRepository studySessionRepository;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		studySessionRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("Param 값이 없을 때, 서버 시간 기준으로 유저의 날짜(Year, Month, Day)에 대한 공부 시간 조회")
	@Test
	void getStudyDuration() {
		// given
		LocalDate now = LocalDate.of(2024, 9, 20);
		User user = createUser("username", "password", "nickname", "디자인학부");
		userRepository.save(user);

        /*
        2024년 - 3시간 3분
        2024년 9월 - 3시간 3분
        2024년 9월 19일 - 2시간 2분, 2024년 9월 20일 - 1시간 1분
         */
		LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
		LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 11);
		LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
		LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 11);
		LocalDateTime startTime3 = LocalDateTime.of(2024, 9, 20, 20, 10);
		LocalDateTime endTime3 = LocalDateTime.of(2024, 9, 20, 21, 11);
		StudySession studySession1 = createStudySession(user, startTime1, endTime1);
		StudySession studySession2 = createStudySession(user, startTime2, endTime2);
		StudySession studySession3 = createStudySession(user, startTime3, endTime3);
		studySessionRepository.saveAll(List.of(studySession1, studySession2, studySession3));

		// when
		StudyDurationResponse studyDurationResponse = studyRecordService.getStudyDuration(now, user.getId());

		// then
		assertThat(studyDurationResponse).isNotNull();
		assertThat(studyDurationResponse)
				.extracting("yearHours", "yearMinutes")
				.containsExactly(
						3L, 3L
				);
		assertThat(studyDurationResponse)
				.extracting("monthHours", "monthMinutes")
				.containsExactly(
						3L, 3L
				);
		assertThat(studyDurationResponse)
				.extracting("dayHours", "dayMinutes")
				.containsExactly(
						1L, 1L
				);
	}

	@DisplayName("Param 값이 있을 때, 날짜(Year, Month, Day)에 대한 공부 시간 조회")
	@Test
	void getStudyDurationWithLocalDate() {
		// given
		LocalDate date = LocalDate.of(2024, 8, 20);
		User user = createUser("username", "password", "nickname", "디자인학부");
		userRepository.save(user);

        /*
        2024년 - 3시간 3분
        2024년 8월 - 2시간 2분
        2024년 8월 19일 - 1시간 1분
        2024년 8월 20일 - 1시간 1분
         */
		LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
		LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 11);
		LocalDateTime startTime2 = LocalDateTime.of(2024, 8, 19, 20, 10);
		LocalDateTime endTime2 = LocalDateTime.of(2024, 8, 19, 21, 11);
		LocalDateTime startTime3 = LocalDateTime.of(2024, 8, 20, 20, 10);
		LocalDateTime endTime3 = LocalDateTime.of(2024, 8, 20, 21, 11);
		StudySession studySession1 = createStudySession(user, startTime1, endTime1);
		StudySession studySession2 = createStudySession(user, startTime2, endTime2);
		StudySession studySession3 = createStudySession(user, startTime3, endTime3);
		studySessionRepository.saveAll(List.of(studySession1, studySession2, studySession3));

		// when
		StudyDurationResponse studyDurationResponse = studyRecordService.getStudyDuration(date, user.getId());

		// then
		assertThat(studyDurationResponse).isNotNull();
		assertThat(studyDurationResponse)
				.extracting("yearHours", "yearMinutes")
				.containsExactly(
						3L, 3L
				);
		assertThat(studyDurationResponse)
				.extracting("monthHours", "monthMinutes")
				.containsExactly(
						2L, 2L
				);
		assertThat(studyDurationResponse)
				.extracting("dayHours", "dayMinutes")
				.containsExactly(
						1L, 1L
				);
	}

	@DisplayName("유저의 특정 날짜(Month, Day)에 대한 공부 횟수 조회")
	@Test
	void getStudyCount() {
		// given
		User user = createUser("username", "password", "nickname", "디자인학부");
		userRepository.save(user);

		LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
		LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
		LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
		LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
		LocalDateTime startTime3 = LocalDateTime.of(2024, 9, 20, 20, 10);
		LocalDateTime endTime3 = LocalDateTime.of(2024, 9, 20, 21, 10);
		StudySession studySession1 = createStudySession(user, startTime1, endTime1);
		StudySession studySession2 = createStudySession(user, startTime2, endTime2);
		StudySession studySession3 = createStudySession(user, startTime3, endTime3);
		studySessionRepository.saveAll(List.of(studySession1, studySession2, studySession3));

		// when
		List<StudyCountLocalDateResponse> result = studyRecordService.getStudyCount(2024, 9, user.getId());

		// then
		assertThat(result).hasSize(2)
				.extracting("date", "studyCount")
				.containsExactlyInAnyOrder(
						tuple(LocalDate.of(2024, 9, 19), 2L),
						tuple(LocalDate.of(2024, 9, 20), 1L)
				);
	}

	@DisplayName("현재 날짜 기준으로 주단위 월, 일, 공부 횟수를 가져옵니다. 순서 보장")
	@Test
	void getStudyCountOfWeek() {
		// given
		User user = createUser("username", "password", "nickname", "디자인학부");
		userRepository.save(user);

		// 9월 16일 월요일 기준 16 ~ 22일 데이터가 출력된다.
		LocalDate today = LocalDate.of(2024, 9, 16);

		LocalDateTime startTime1 = LocalDateTime.of(2024, 9, 19, 10, 10);
		LocalDateTime endTime1 = LocalDateTime.of(2024, 9, 19, 11, 10);
		LocalDateTime startTime2 = LocalDateTime.of(2024, 9, 19, 20, 10);
		LocalDateTime endTime2 = LocalDateTime.of(2024, 9, 19, 21, 10);
		LocalDateTime startTime3 = LocalDateTime.of(2024, 9, 20, 20, 10);
		LocalDateTime endTime3 = LocalDateTime.of(2024, 9, 20, 21, 10);
		StudySession studySession1 = createStudySession(user, startTime1, endTime1);
		StudySession studySession2 = createStudySession(user, startTime2, endTime2);
		StudySession studySession3 = createStudySession(user, startTime3, endTime3);
		studySessionRepository.saveAll(List.of(studySession1, studySession2, studySession3));

		// when
		List<StudyCountResponse> result = studyRecordService.getStudyCountOfWeek(today, user.getId());

		// then
		assertThat(result).hasSize(7)
				.extracting("date", "studyCount")
				.containsExactly(
						tuple("9/16", 0L),
						tuple("9/17", 0L),
						tuple("9/18", 0L),
						tuple("9/19", 2L),
						tuple("9/20", 1L),
						tuple("9/21", 0L),
						tuple("9/22", 0L)

				);
	}

	private User createUser(final String username, final String password, final String nickname, final String department) {
		return User.builder()
				.username(username)
				.password(password)
				.role(Role.USER)
				.department(department)
				.nickname(nickname)
				.build();
	}

	private StudySession createStudySession(User user, final LocalDateTime startTime, final LocalDateTime endTime) {
		return StudySession.builder()
				.user(user)
				.startTime(startTime)
				.endTime(endTime)
				.build();
	}
}
