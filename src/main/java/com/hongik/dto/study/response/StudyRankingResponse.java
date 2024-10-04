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

    private int previousRank;

    private int rankChange;

    @Builder
    public StudyRankingResponse(final String department, final Long studyDurationOfWeek, final int currentRank, final int previousRank, final int rankChange) {
        this.department = department;
        this.studyDurationOfWeek = studyDurationOfWeek;
        this.currentRank = currentRank;
        this.previousRank = previousRank;
        this.rankChange = rankChange;
    }

    public static StudyRankingResponse of(final String department, final Long studyDurationOfWeek, final int currentRank, final int previousRank) {
        return StudyRankingResponse.builder()
                .department(department)
                .studyDurationOfWeek(studyDurationOfWeek)
                .currentRank(currentRank)
                .previousRank(previousRank)
                .rankChange(previousRank - currentRank)
                .build();
    }
}
