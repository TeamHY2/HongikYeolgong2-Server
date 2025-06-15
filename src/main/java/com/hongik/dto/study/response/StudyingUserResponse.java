package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyingUserResponse {
	@Schema(example = "1")
	private Long userId;

	@Schema(example = "kim")
	private String userName;

	@Schema(example = "14:30:00")
	private LocalTime studyDuration;

	@Schema(example = "true")
	private boolean studyStatus;


	@Builder
	public StudyingUserResponse(final Long userId, final String userName, final LocalTime studyDuration,
								final boolean studyStatus) {
		this.userId = userId;
		this.userName = userName;
		this.studyDuration = studyDuration;
		this.studyStatus = studyStatus;
	}

	public static StudyingUserResponse of(final Long userId, final String userName, final LocalTime studyDuration,
										  final boolean studyStatus) {
		return StudyingUserResponse.builder()
				.userId(userId)
				.userName(userName)
				.studyDuration(studyDuration)
				.studyStatus(studyStatus)
				.build();
	}
}
