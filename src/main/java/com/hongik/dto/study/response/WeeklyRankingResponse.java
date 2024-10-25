package com.hongik.dto.study.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeeklyRankingResponse {

    private String weekName;

    private List<StudyRankingResponse> departmentRankings;

    @Builder
    public WeeklyRankingResponse(final String weekName, final List<StudyRankingResponse> departmentRankings) {
        this.weekName = weekName;
        this.departmentRankings = departmentRankings;
    }

    public static WeeklyRankingResponse of(final String weekName, final List<StudyRankingResponse> departmentRankings) {
        return WeeklyRankingResponse.builder()
                .weekName(weekName)
                .departmentRankings(departmentRankings)
                .build();
    }
}
