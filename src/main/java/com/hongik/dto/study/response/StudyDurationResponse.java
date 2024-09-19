package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyDurationResponse {

    @Schema(example = "10")
    private Long dailyStudyDuration;

    @Schema(example = "20")
    private Long monthlyStudyDuration;

    @Builder
    public StudyDurationResponse(final Long dailyStudyDuration, final Long monthlyStudyDuration) {
        this.dailyStudyDuration = dailyStudyDuration;
        this.monthlyStudyDuration = monthlyStudyDuration;
    }

    public static StudyDurationResponse of(final Long dailyStudyDuration, final Long monthlyStudyDuration) {
        return StudyDurationResponse.builder()
                .dailyStudyDuration(dailyStudyDuration)
                .monthlyStudyDuration(monthlyStudyDuration)
                .build();
    }
}
