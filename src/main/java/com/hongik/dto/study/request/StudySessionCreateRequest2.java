package com.hongik.dto.study.request;

import com.hongik.domain.study.StudySession;
import com.hongik.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudySessionCreateRequest2 {

    @Schema(example = "2024-09-19T14:30:00")
    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalDateTime startTime;

    @Builder
    public StudySessionCreateRequest2(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public StudySession toEntity(User user, boolean studyStatus) {
        return StudySession.builder()
                .startTime(startTime)
                .user(user)
                .studyStatus(studyStatus)
                .build();
    }
}
