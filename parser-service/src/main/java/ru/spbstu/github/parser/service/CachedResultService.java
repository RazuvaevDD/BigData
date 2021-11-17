package ru.spbstu.github.parser.service;

import ru.spbstu.github.parser.api.dto.MonthDaysDto;
import ru.spbstu.github.parser.api.dto.YearMonthsDto;
import ru.spbstu.github.parser.api.dto.YearWeeksDto;

public interface CachedResultService {

    YearMonthsDto getCountOfRegistrationsByMonths();

    YearWeeksDto getCountOfRegistrationsByWeeks();

    MonthDaysDto getCountOfRegistrationsByDays(int month);
}
