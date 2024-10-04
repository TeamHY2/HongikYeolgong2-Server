package com.hongik.dto.study.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RankResponse {

    private String department;

    private Long studyDurationOfWeek;

    private int currentRanking;


    @Builder
    public RankResponse(final String department, final Long studyDurationOfWeek, final int currentRanking) {
        this.department = department;
        this.studyDurationOfWeek = studyDurationOfWeek;
        this.currentRanking = currentRanking;
    }

    public static RankResponse of(final String department, final Long studyDurationOfWeek, final int currentRanking) {
        return RankResponse.builder()
                .department(department)
                .studyDurationOfWeek(studyDurationOfWeek)
                .currentRanking(currentRanking)
                .build();
    }
}
