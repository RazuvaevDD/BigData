package ru.spbstu.github.parser.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.threeten.extra.YearWeek;
import ru.spbstu.github.parser.api.dto.MonthDaysDto;
import ru.spbstu.github.parser.api.dto.YearMonthsDto;
import ru.spbstu.github.parser.api.dto.YearWeeksDto;
import ru.spbstu.github.parser.dao.model.result.MonthDaysEntity;
import ru.spbstu.github.parser.dao.model.result.YearMonthsEntity;
import ru.spbstu.github.parser.dao.model.result.YearWeeksEntity;
import ru.spbstu.github.parser.dao.repository.*;
import ru.spbstu.github.parser.service.ResultService;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static ru.spbstu.github.parser.helper.EndpointHelper.*;

@Slf4j
@Service
@AllArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final UserDetailRepository repository;
    private final UserRepository userRepository;
    private final MonthDaysRepository monthDaysRepository;
    private final YearMonthsRepository yearMonthsRepository;
    private final YearWeeksRepository yearWeeksRepository;

    @Override
    public YearMonthsDto getCountOfRegistrationsByMonths() {
        var result = new HashMap<Integer, List<Integer>>();
        for (int i = 1; i < 12; i++) {
            Instant from = new DateTime(2021, i, 1, 1, 1).toDate().toInstant();
            Instant to = new DateTime(2021, i, daysOfMonth(i), 1, 1).toDate().toInstant();
            var usersCount = repository.countByCreatedAtBetweenAndType(from, to, "User");
            var organizationsCount = repository.countByCreatedAtBetweenAndType(from, to, "Organization");
            log.info("Month {}. Extracting users from {} to {}. Users count={}", i, from, to, usersCount);
            log.info("Month {}. Extracting users from {} to {}. Organizations count={}", i, from, to, organizationsCount);
            result.put(i, List.of(usersCount, organizationsCount));
        }
        log.info("Finished.");
        YearMonthsDto dto = new YearMonthsDto();
        dto.setMonths(result);
        dto.setYear(2021);
        YearMonthsEntity entity = new YearMonthsEntity();
        entity.setId(BY_MONTH);
        entity.setYear(dto.getYear());
        entity.setMonths(dto.getMonths());
        yearMonthsRepository.save(entity);
        return dto;
    }

    @Override
    public YearWeeksDto getCountOfRegistrationsByWeeks() {
        var weeks = new HashMap<Integer, List<Integer>>();
        //YearWeek start = YearWeek.of(2021, 1);

        for (int i = 1; i < 53; i++) {
            var currentWeek = YearWeek.of(2021, i);
            var from = currentWeek.atDay(DayOfWeek.MONDAY).atStartOfDay().toInstant(UTC);
            var to = from.plusSeconds(604800);
            var usersCount = repository.countByCreatedAtBetweenAndType(from, to, "User");
            var organizationsCount = repository.countByCreatedAtBetweenAndType(from, to, "Organization");
            log.info("Week {}. Extracting users from {} to {}. Users count={}", i, from, to, usersCount);
            log.info("Week {}. Extracting users from {} to {}. Organizations count={}", i, from, to, organizationsCount);
            weeks.put(i, List.of(usersCount, organizationsCount));
        }
        log.info("Extracted count of registrations by week.");
        YearWeeksDto dto = new YearWeeksDto();
        dto.setYear(2021);
        dto.setWeeks(weeks);
        YearWeeksEntity entity = new YearWeeksEntity();
        entity.setId(BY_WEEKS);
        entity.setYear(dto.getYear());
        entity.setWeeks(dto.getWeeks());
        yearWeeksRepository.save(entity);
        return dto;
    }

    public MonthDaysDto getCountOfRegistrationsByDays(int month) {
        if (month < 0 || month > 12)
            return new MonthDaysDto();

        var dto = new MonthDaysDto();
        var result = new HashMap<Integer, List<Integer>>();

        var lastDayOfMonth = daysOfMonth(month);

        for (int i = 1; i <= lastDayOfMonth; i++) {
            var currentDay = MonthDay.of(month, i);
            var from = MonthDay.of(month, i).atYear(2021).atStartOfDay().toInstant(UTC);
            var to = from.plusSeconds(86399); // 24 hours = 86400 seconds
            var usersCount = repository.countByCreatedAtBetweenAndType(from, to, "User");
            var organizationsCount = repository.countByCreatedAtBetweenAndType(from, to, "Organization");
            log.info("Day {}. Extracting users from {} to {}. Users count={}", i, from, to, usersCount);
            log.info("Day {}. Extracting users from {} to {}. Organizations count={}", i, from, to, organizationsCount);
            result.put(i, List.of(usersCount, organizationsCount));
        }
        log.info("Extracted count of registrations by days of month {}.", month);

        dto.setYear(2021);
        dto.setMonth(Month.of(month));
        dto.setDays(result);
        MonthDaysEntity entity = new MonthDaysEntity();
        entity.setId(BY_DAY + month);
        entity.setYear(dto.getYear());
        entity.setDays(dto.getDays());
        monthDaysRepository.save(entity);
        return dto;
    }

    private static int daysOfMonth(int month) {
        DateTime dateTime = new DateTime(2021, month, 1, 1, 1, 1, 1);
        return dateTime.dayOfMonth().getMaximumValue();
    }
}
