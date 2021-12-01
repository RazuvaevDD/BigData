package ru.spbstu.github.parser.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.github.parser.api.dto.MonthDaysDto;
import ru.spbstu.github.parser.api.dto.YearMonthsDto;
import ru.spbstu.github.parser.api.dto.YearWeeksDto;
import ru.spbstu.github.parser.service.CachedResultService;

import java.util.List;

@RestController
@RequestMapping("/cached/result")
@AllArgsConstructor
@Slf4j
public class CachedResultController {

    private final CachedResultService cachedResultService;

    @GetMapping("/month")
    public YearMonthsDto retrieveMonths() {
        return cachedResultService.getCountOfRegistrationsByMonths();
    }

    @GetMapping("/week")
    public YearWeeksDto retrieveWeeks() {
        return cachedResultService.getCountOfRegistrationsByWeeks();
    }

    @GetMapping("/day")
    public MonthDaysDto retrieveDaysByMonth(@RequestParam int month) {
        return cachedResultService.getCountOfRegistrationsByDays(month);
    }

    // Parsed

    @GetMapping("/month/parsed")
    public List<List<Object>> retrieveMonthParsed() {
        return cachedResultService.getCountOfRegistrationsByMonthsParsed();
    }

    @GetMapping("/week/parsed")
    public List<List<Object>> retrieveWeeksParsed() {
        return cachedResultService.getCountOfRegistrationsByWeeksParsed();
    }

    @GetMapping("/day/parsed")
    public List<List<Object>> retrieveDaysByMonthParsed(@RequestParam int month) {
        return cachedResultService.getCountOfRegistrationsByDaysParsed(month);
    }
}
