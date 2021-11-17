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
import ru.spbstu.github.parser.service.ResultService;

@RestController
@RequestMapping("/result")
@AllArgsConstructor
@Slf4j
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/month")
    public YearMonthsDto retrieveMonths() {
        return resultService.getCountOfRegistrationsByMonths();
    }

    @GetMapping("/week")
    public YearWeeksDto retrieveWeeks() {
        return resultService.getCountOfRegistrationsByWeeks();
    }

    @GetMapping("/day")
    public MonthDaysDto retrieveDaysByMonth(@RequestParam int month) {
        return resultService.getCountOfRegistrationsByDays(month);
    }
}
