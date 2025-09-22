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
public class StudySessionStartResponse {

    @Schema(example = "1")
    private Long studySessionId;

    @Schema(example = "2")
    private Long userId;

    @Schema(example = "2024-09-19T14:30:00")
    private LocalDateTime startTime;


    @Builder
    public StudySessionStartResponse(final Long studySessionId, final Long userId, final LocalDateTime startTime) {
        this.studySessionId = studySessionId;
        this.userId = userId;
        this.startTime = startTime;
    }

    public static StudySessionStartResponse of(StudySession studySession) {
        return StudySessionStartResponse.builder()
                .studySessionId(studySession.getId())
                .startTime(studySession.getStartTime())
                .userId(studySession.getUser().getId())
                .build();
    }
}
