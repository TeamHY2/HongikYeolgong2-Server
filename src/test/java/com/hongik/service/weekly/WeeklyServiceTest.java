package com.hongik.service.weekly;

import com.hongik.domain.user.UserRepository;
import com.hongik.domain.weekly.Weekly;
import com.hongik.domain.weekly.WeeklyRepository;
import com.hongik.dto.weekly.response.WeeklyResponse;
import com.hongik.exception.AppException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class WeeklyServiceTest {

    @Autowired
    private WeeklyService weeklyService;

    @Autowired
    private WeeklyRepository weeklyRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        weeklyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("입력한 연도에 대한 주차 데이터를 생성합니다.")
    @Test
    void createWeekFields() {
        // given
        final int year = 2024;

        // when
        List<WeeklyResponse> result = weeklyService.createWeekFields(year);

        // then
        assertThat(result).isNotEmpty();
    }

    @DisplayName("입력한 연도에 대한 주차 데이터를 생성할 때, 이미 존재하면 예외가 발생한다.")
    @Test
    void createWeekFieldsWithDuplicateYear() {
        // given
        final int year = 2024;
        Weekly weekly = createWeekFields(year);
        weeklyRepository.save(weekly);

        // when // then
        assertThatThrownBy(() -> weeklyService.createWeekFields(year))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 연도에 주차데이터입니다.");
    }

    private Weekly createWeekFields(final int year) {
        return Weekly.builder()
                .years(year)
                .weekNumber(202401)
                .weekName("weekName")
                .build();
    }
}