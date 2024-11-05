package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyRankingResponse {

    @Schema(example = "컴퓨터공학과")
    private String department;

    @Schema(example = "24")
    private Long studyDurationOfWeek;

    @Schema(example = "2")
    private int currentRank;

    @Schema(example = "-1")
    private int rankChange;

    @Builder
    public StudyRankingResponse(final String department, final Long studyDurationOfWeek, final int currentRank, final int rankChange) {
        this.department = department;
        this.studyDurationOfWeek = studyDurationOfWeek;
        this.currentRank = currentRank;
        this.rankChange = rankChange;
    }

    public static StudyRankingResponse of(final String department, final Long studyDurationOfWeek, final int currentRank, final int previousRank) {
        return StudyRankingResponse.builder()
                .department(department)
                .studyDurationOfWeek(studyDurationOfWeek)
                .currentRank(currentRank)
                .rankChange(previousRank - currentRank)
                .build();
    }
}
