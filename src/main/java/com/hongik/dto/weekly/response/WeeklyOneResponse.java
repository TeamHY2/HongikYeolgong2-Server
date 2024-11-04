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

    @Schema(example = "202443")
    private int weekNumber;

    @Builder
    public WeeklyOneResponse(final int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public static WeeklyOneResponse of(int weekNumber) {
        return WeeklyOneResponse.builder()
                .weekNumber(weekNumber)
                .build();
    }
}
