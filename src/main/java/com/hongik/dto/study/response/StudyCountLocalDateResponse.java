package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyCountLocalDateResponse {

    @Schema(example = "2024-09-19")
    private LocalDate date;

    @Schema(example = "3")
    private Long studyCount;

    @Builder
    public StudyCountLocalDateResponse(final LocalDate date, final Long studyCount) {
        this.date = date;
        this.studyCount = studyCount;
    }

    public static StudyCountLocalDateResponse of(final LocalDate date, final Long studyCount) {
        return StudyCountLocalDateResponse.builder()
                .date(date)
                .studyCount(studyCount)
                .build();
    }
}
