package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyDurationResponse {

    @Schema(example = "120")
    private Long studyDurationWithYear;

    @Schema(example = "20")
    private Long studyDurationWithMonth;

    @Schema(example = "10")
    private Long studyDurationWithDay;

    @Schema(example = "50")
    private Long studyDurationWithSemester;

    @Builder
    public StudyDurationResponse(final Long studyDurationWithYear, final Long studyDurationWithMonth, final Long studyDurationWithDay, final Long studyDurationWithSemester) {
        this.studyDurationWithYear = studyDurationWithYear;
        this.studyDurationWithMonth = studyDurationWithMonth;
        this.studyDurationWithDay = studyDurationWithDay;
        this.studyDurationWithSemester = studyDurationWithSemester;
    }

    public static StudyDurationResponse of(final Long studyDurationWithYear, final Long studyDurationWithMonth, final Long studyDurationWithDay, final Long studyDurationWithSemester) {
        return StudyDurationResponse.builder()
                .studyDurationWithYear(studyDurationWithYear)
                .studyDurationWithMonth(studyDurationWithMonth)
                .studyDurationWithDay(studyDurationWithDay)
                .studyDurationWithSemester(studyDurationWithSemester)
                .build();
    }
}
