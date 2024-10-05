package com.hongik.domain.weekly;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Weekly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int years; // year로 쓰고 싶은데 어쩔 수 없이 years로 사용

    private String weekName; // 1월 1주차, 2주차 ...

    private int weekNumber; // 202401, 202402, ...

    @Builder
    public Weekly(final int years, final String weekName, final int weekNumber) {
        this.years = years;
        this.weekName = weekName;
        this.weekNumber = weekNumber;
    }
}
