package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    private Long semesterHours;

    private Long semesterMinutes;

    @Builder
    public StudyDurationResponse(final Long yearHours, final Long yearMinutes, final Long monthHours, final Long monthMinutes, final Long dayHours, final Long dayMinutes, final Long semesterHours, final Long semesterMinutes) {
        this.yearHours = yearHours;
        this.yearMinutes = yearMinutes;
        this.monthHours = monthHours;
        this.monthMinutes = monthMinutes;
        this.dayHours = dayHours;
        this.dayMinutes = dayMinutes;
        this.semesterHours = semesterHours;
        this.semesterMinutes = semesterMinutes;
    }

    public static StudyDurationResponse of(final Long yearHours, final Long yearMinutes, final Long monthHours, final Long monthMinutes, final Long dayHours, final Long dayMinutes, final Long semesterHours, final Long semesterMinutes) {
        return StudyDurationResponse.builder()
                .yearHours(yearHours)
                .yearMinutes(yearMinutes)
                .monthHours(monthHours)
                .monthMinutes(monthMinutes)
                .dayHours(dayHours)
                .dayMinutes(dayMinutes)
                .semesterHours(semesterHours)
                .semesterMinutes(semesterMinutes)
                .build();
    }
}
