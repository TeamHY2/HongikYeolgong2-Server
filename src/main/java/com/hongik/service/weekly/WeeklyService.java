package com.hongik.service.weekly;

import com.hongik.domain.weekly.Weekly;
import com.hongik.domain.weekly.WeeklyRepository;
import com.hongik.dto.weekly.response.WeeklyOneResponse;
import com.hongik.dto.weekly.response.WeeklyResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class WeeklyService {

    private final WeeklyRepository weeklyRepository;

    @Transactional
    public List<WeeklyResponse> createWeekFields(final int year) {
        if (weeklyRepository.existsByYears(year)) {
            throw new AppException(ErrorCode.ALREADY_EXIST_WEEK, ErrorCode.ALREADY_EXIST_WEEK.getMessage());
        }

        Map<String, Integer> map = new LinkedHashMap<>();

        List<LocalDate> datesIn2024 = getAllDatesInYear(year);
        for (LocalDate localDate : datesIn2024) {
            String print = getCurrentWeekOfMonth(localDate);
            String[] split = print.split("=");
            if (!map.containsKey(split[0])) {
                map.put(split[0], Integer.parseInt(split[1]));
            }
        }

        List<Weekly> weekInfoList = new ArrayList<>();
        for (String key : map.keySet()) {
            weekInfoList.add(
                    Weekly.builder()
                            .years(year)
                            .weekName(key)
                            .weekNumber(map.get(key))
                            .build()
            );
        }

//        System.out.println("weekInfoList = " + weekInfoList.size());
        List<Weekly> weeklyList = weeklyRepository.saveAll(weekInfoList);
        return weeklyList.stream()
                .map(WeeklyResponse::of)
                .toList();
    }

    private String getCurrentWeekOfMonth(LocalDate localDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);

        int weekOfMonth = localDate.get(weekFields.weekOfMonth());

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            // 전 달의 마지막 날 기준
            LocalDate lastDayOfLastMonth = localDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
            return getCurrentWeekOfMonth(lastDayOfLastMonth);
        }

        // 이번 달의 마지막 날 기준
        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        // 마지막 주차의 경우 마지막 날이 월~수 사이이면 다음달 1주차로 계산
        if (weekOfMonth == lastDayOfMonth.get(weekFields.weekOfMonth()) && lastDayOfMonth.getDayOfWeek().compareTo(DayOfWeek.THURSDAY) < 0) {
            LocalDate firstDayOfNextMonth = lastDayOfMonth.plusDays(1); // 마지막 날 + 1일 => 다음달 1일
            return getCurrentWeekOfMonth(firstDayOfNextMonth);
        }

        int result = localDate.getYear() * 100 + localDate.get(weekFields.weekOfYear());
        return localDate.getMonthValue() + "월 " + weekOfMonth + "주차=" + result;
    }

    private List<LocalDate> getAllDatesInYear(int year) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        // 모든 날짜 추가
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dates.add(date);
        }

        return dates;
    }

    public List<WeeklyResponse> getWeeklyFields(final int year) {
        List<Weekly> weeklyList = weeklyRepository.findAllByYears(year);

        return weeklyList.stream()
                .map(WeeklyResponse::of)
                .toList();
    }

    public WeeklyOneResponse getWeekly(final LocalDate localDate) {
        String currentWeekOfMonth = getCurrentWeekOfMonth(localDate);
        int weekNumber = Integer.parseInt(currentWeekOfMonth.split("=")[1]);


        return WeeklyOneResponse.of(weekNumber);
    }
}
