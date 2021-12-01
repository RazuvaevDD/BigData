package ru.spbstu.github.parser.service;

import ru.spbstu.github.parser.api.dto.MonthDaysDto;
import ru.spbstu.github.parser.api.dto.YearMonthsDto;
import ru.spbstu.github.parser.api.dto.YearWeeksDto;

import java.util.List;

public interface CachedResultService {

    YearMonthsDto getCountOfRegistrationsByMonths();

    YearWeeksDto getCountOfRegistrationsByWeeks();

    MonthDaysDto getCountOfRegistrationsByDays(int month);

    List<List<Object>> getCountOfRegistrationsByMonthsParsed();

    List<List<Object>> getCountOfRegistrationsByWeeksParsed();

    List<List<Object>> getCountOfRegistrationsByDaysParsed(int month);
}
