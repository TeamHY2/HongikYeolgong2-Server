package com.hongik.dto.weekly.response;

import com.hongik.domain.weekly.Weekly;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeeklyResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "2024")
    private int year;

    @Schema(example = "10월 5주차")
    private String weekName; // 1월 1주차, 2주차 ...

    @Schema(example = "202443")
    private int weekNumber; // 202401, 202402, ...

    @Builder
    public WeeklyResponse(final Long id, final int year, final String weekName, final int weekNumber) {
        this.id = id;
        this.year = year;
        this.weekName = weekName;
        this.weekNumber = weekNumber;
    }

    public static WeeklyResponse of(Weekly weekly) {
        return WeeklyResponse.builder()
                .id(weekly.getId())
                .year(weekly.getYears())
                .weekName(weekly.getWeekName())
                .weekNumber(weekly.getWeekNumber())
                .build();
    }
}
