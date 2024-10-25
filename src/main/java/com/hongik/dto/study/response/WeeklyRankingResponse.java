package com.hongik.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeeklyRankingResponse {

    @Schema(example = "10월 4주차")
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
