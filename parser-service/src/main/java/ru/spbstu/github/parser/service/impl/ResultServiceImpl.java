package ru.spbstu.github.parser.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.threeten.extra.YearWeek;
import ru.spbstu.github.parser.api.dto.MonthDaysDto;
import ru.spbstu.github.parser.api.dto.YearMonthsDto;
import ru.spbstu.github.parser.api.dto.YearWeeksDto;
import ru.spbstu.github.parser.dao.repository.UserDetailRepository;
import ru.spbstu.github.parser.service.ResultService;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
@AllArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final UserDetailRepository repository;

    private static final Map<Integer, Instant> months = new HashMap<>();

    @Override
    public YearMonthsDto getCountOfRegistrationsByMonths() {
        var result = new HashMap<Integer, Integer>();
        for (int i = 1; i < 12; i++) {
            Instant from = new DateTime(2021, 0, 1, 1, 1, 1, 1).toDate().toInstant();
            Instant to = new DateTime(2021, 11, 30, 1, 1, 1, 1).toDate().toInstant();
            var count = repository.countUserDetailByCreatedAtIsBetween(from, to);
            log.info("Month {}. Extracting users from {} to {}. Count={}", i, from, to, count);
            result.put(i, count);
        }
        log.info("Finished.");
        YearMonthsDto year = new YearMonthsDto();
        year.setMonths(result);
        return year;
    }

    @Override
    public YearWeeksDto getCountOfRegistrationsByWeeks() {
        var weeks = new HashMap<Integer, Integer>();
        YearWeek start = YearWeek.of(2021, 1);

        for (int i = 1; i < start.lengthOfYear(); i++) {
            var currentWeek = YearWeek.of(2021, i);
            var from = currentWeek.atDay(DayOfWeek.MONDAY).atStartOfDay().toInstant(UTC);
            var to = currentWeek.atDay(DayOfWeek.SUNDAY).atStartOfDay().toInstant(UTC);
            var count = repository.countUserDetailByCreatedAtIsBetween(from, to);
            log.info("Week {}. Extracting users from {} to {}. Count={}", i, from, to, count);
            weeks.put(i, count);
        }
        log.info("Extracted count of registrations by week.");
        YearWeeksDto week = new YearWeeksDto();
        week.setYear(2021);
        week.setWeeks(weeks);
        return week;
    }

    public MonthDaysDto getCountOfRegistrationsByDays(int month) {
        if (month < 0 || month > 12)
            return new MonthDaysDto();

        var monthDto = new MonthDaysDto();
        var result = new HashMap<Integer, Integer>();

        var lastDayOfMonth = daysOfMonth(month);

        for (int i = 1; i < lastDayOfMonth - 1; i++) {
            var currentDay = MonthDay.of(month, i);
            var from = MonthDay.of(month, i).atYear(2021).atStartOfDay().toInstant(UTC);
            var to = MonthDay.of(month, i + 1).atYear(2021).atStartOfDay().toInstant(UTC);
            var count = repository.countUserDetailByCreatedAtIsBetween(from, to);
            log.info("Day {}. Extracting users from {} to {}. Count={}", i, from, to, count);
            result.put(i, count);
        }
        log.info("Extracted count of registrations by days of month {}.", month);

        monthDto.setYear(2021);
        monthDto.setMonth(Month.of(month));
        monthDto.setDays(result);
        return monthDto;
    }

    private static int daysOfMonth(int month) {
        DateTime dateTime = new DateTime(2021, month, 1, 1, 1, 1, 1);
        return dateTime.dayOfMonth().getMaximumValue();
    }
}
