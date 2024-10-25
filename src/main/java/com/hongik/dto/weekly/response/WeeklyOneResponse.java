package com.hongik.dto.weekly.response;

import com.hongik.domain.weekly.Weekly;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeeklyOneResponse {

    private int year;

    private String weekName;

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
