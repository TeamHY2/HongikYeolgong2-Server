package com.hongik.dto.study.response;

import com.hongik.domain.study.StudySession;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudySessionResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "2")
    private Long userId;

    @Schema(example = "2024-09-19T14:30:00")
    private LocalDateTime startTime;

    @Schema(example = "2024-09-19T14:35:00")
    private LocalDateTime endTime;

    @Builder
    public StudySessionResponse(final Long id, final Long userId, final LocalDateTime startTime, final LocalDateTime endTime) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static StudySessionResponse of(StudySession studySession) {
        return StudySessionResponse.builder()
                .id(studySession.getId())
                .startTime(studySession.getStartTime())
                .endTime(studySession.getEndTime())
                .userId(studySession.getUser().getId())
                .build();
    }
}
