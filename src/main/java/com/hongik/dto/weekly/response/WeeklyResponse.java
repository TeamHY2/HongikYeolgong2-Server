package com.hongik.dto.weekly.response;

import com.hongik.domain.weekly.Weekly;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeeklyResponse {

    private Long id;

    private int year;

    private String weekName; // 1월 1주차, 2주차 ...

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
