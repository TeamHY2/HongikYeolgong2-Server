package com.hongik.dto.study.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyDurationResponse {

    private Long dailyStudyDuration;

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
