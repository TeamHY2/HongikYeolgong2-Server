package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyCountResponse2 {

    @Schema(example = "9/19")
    private String date;

    @Schema(example = "3")
    private Long studyCount;

    @Builder
    public StudyCountResponse2(final String date, final Long studyCount) {
        this.date = date;
        this.studyCount = studyCount;
    }

    public static StudyCountResponse2 of(final String date, final Long studyCount) {
        return StudyCountResponse2.builder()
                .date(date)
                .studyCount(studyCount)
                .build();
    }
}
