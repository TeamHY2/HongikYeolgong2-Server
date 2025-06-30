package com.hongik.dto.study.response;

import com.hongik.domain.study.StudySession;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EndStudySessionResponse {
	@Schema(example = "1")
	private Long studySessionId;

	@Schema(example = "2")
	private Long userId;

	@Schema(example = "2024-09-19T14:30:00")
	private LocalDateTime endTime;


	@Builder
	public EndStudySessionResponse(final Long studySessionId, final Long userId, final LocalDateTime endTime) {
		this.studySessionId = studySessionId;
		this.userId = userId;
		this.endTime = endTime;
	}

	public static EndStudySessionResponse of(StudySession studySession) {
		return EndStudySessionResponse.builder()
				.studySessionId(studySession.getId())
				.userId(studySession.getUser().getId())
				.endTime(studySession.getEndTime())
				.build();
	}
}
