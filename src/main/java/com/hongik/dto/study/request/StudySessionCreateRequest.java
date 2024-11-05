package com.hongik.dto.study.request;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudySessionCreateRequest {

    @Schema(example = "2024-09-19T14:30:00")
    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalDateTime startTime;

    @Schema(example = "2024-09-19T14:35:00")
    @NotNull(message = "끝나는 시간은 필수입니다.")
    private LocalDateTime endTime;

    @Builder
    public StudySessionCreateRequest(final LocalDateTime startTime, final LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public StudySession toEntity(User user) {
        return StudySession.builder()
                .startTime(startTime)
                .endTime(endTime)
                .user(user)
                .build();
    }
}
