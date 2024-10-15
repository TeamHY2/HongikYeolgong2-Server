package com.hongik.migration.firebase;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class StudyDay {

    private String id;

    private LocalDateTime start;
    private LocalDateTime end;

    private int duration;

    @Builder
    public StudyDay(final String id, final LocalDateTime start, final LocalDateTime end, final int duration) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.duration = duration;
    }
}
