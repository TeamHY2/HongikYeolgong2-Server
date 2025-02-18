package com.hongik.dto.study.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyDurationResponse {

    private Long yearHours;

    private Long yearMinutes;

    private Long monthHours;

    private Long monthMinutes;

    private Long dayHours;

    private Long dayMinutes;

    @Builder
    public StudyDurationResponse(final Long yearHours, final Long yearMinutes, final Long monthHours, final Long monthMinutes, final Long dayHours, final Long dayMinutes) {
        this.yearHours = yearHours;
        this.yearMinutes = yearMinutes;
        this.monthHours = monthHours;
        this.monthMinutes = monthMinutes;
        this.dayHours = dayHours;
        this.dayMinutes = dayMinutes;
    }

    public static StudyDurationResponse of(final Long yearHours, final Long yearMinutes, final Long monthHours, final Long monthMinutes, final Long dayHours, final Long dayMinutes) {
        return StudyDurationResponse.builder()
                .yearHours(yearHours)
                .yearMinutes(yearMinutes)
                .monthHours(monthHours)
                .monthMinutes(monthMinutes)
                .dayHours(dayHours)
                .dayMinutes(dayMinutes)
                .build();
    }
}
