package com.hongik.dto.study.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EndStudySessionRequest {
	@Schema(example = "1")
	@NotNull(message = "studySessionId는 필수입니다.")
	private Long studySessionId;

	@Schema(example = "2024-09-19T14:35:00")
	@NotNull(message = "끝나는 시간은 필수입니다.")
	private LocalDateTime endTime;

	@Builder
	public EndStudySessionRequest(final Long studySessionId, final LocalDateTime endTime) {
		this.studySessionId = studySessionId;
		this.endTime = endTime;
	}
}
