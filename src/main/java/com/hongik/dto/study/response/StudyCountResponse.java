package com.hongik.dto.study.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyCountResponse {

    private LocalDate date;

    private Long studyCount;

    @Builder
    public StudyCountResponse(final LocalDate date, final Long studyCount) {
        this.date = date;
        this.studyCount = studyCount;
    }

    public static StudyCountResponse of(final LocalDate date, final Long studyCount) {
        return StudyCountResponse.builder()
                .date(date)
                .studyCount(studyCount)
                .build();
    }
}
