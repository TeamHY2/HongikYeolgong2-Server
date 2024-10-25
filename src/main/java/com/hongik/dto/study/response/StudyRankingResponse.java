package com.hongik.dto.study.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyRankingResponse {

    private String department;

    private Long studyDurationOfWeek;

    private int currentRank;

    private String weekName;

    private int rankChange;

    @Builder
    public StudyRankingResponse(final String department, final Long studyDurationOfWeek, final String weekName, final int currentRank, final int rankChange) {
        this.department = department;
        this.studyDurationOfWeek = studyDurationOfWeek;
        this.currentRank = currentRank;
        this.weekName = weekName;
        this.rankChange = rankChange;
    }

    public static StudyRankingResponse of(final String department, final Long studyDurationOfWeek, final int currentRank, final String weekName, final int previousRank) {
        return StudyRankingResponse.builder()
                .department(department)
                .studyDurationOfWeek(studyDurationOfWeek)
                .currentRank(currentRank)
                .weekName(weekName)
                .rankChange(previousRank - currentRank)
                .build();
    }
}
