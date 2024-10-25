package com.hongik.dto.weekly.response;

import com.hongik.domain.weekly.Weekly;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeeklyOneResponse {

    @Schema(example = "2024")
    private int year;

    @Schema(example = "10월 4주차")
    private String weekName;

    @Schema(example = "202443")
    private int weekNumber;

    @Builder
    public WeeklyOneResponse(final int year, final String weekName, final int weekNumber) {
        this.year = year;
        this.weekName = weekName;
        this.weekNumber = weekNumber;
    }

    public static WeeklyOneResponse of(int year, String weekName, int weekNumber) {
        return WeeklyOneResponse.builder()
                .year(year)
                .weekName(weekName)
                .weekNumber(weekNumber)
                .build();
    }
}
