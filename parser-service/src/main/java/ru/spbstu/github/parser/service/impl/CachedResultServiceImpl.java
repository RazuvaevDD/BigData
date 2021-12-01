package ru.spbstu.github.parser.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.spbstu.github.parser.api.dto.MonthDaysDto;
import ru.spbstu.github.parser.api.dto.YearMonthsDto;
import ru.spbstu.github.parser.api.dto.YearWeeksDto;
import ru.spbstu.github.parser.dao.model.result.MonthDaysEntity;
import ru.spbstu.github.parser.dao.model.result.YearMonthsEntity;
import ru.spbstu.github.parser.dao.model.result.YearWeeksEntity;
import ru.spbstu.github.parser.dao.repository.MonthDaysRepository;
import ru.spbstu.github.parser.dao.repository.YearMonthsRepository;
import ru.spbstu.github.parser.dao.repository.YearWeeksRepository;
import ru.spbstu.github.parser.service.CachedResultService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.spbstu.github.parser.helper.EndpointHelper.*;

@Slf4j
@Service
@AllArgsConstructor
public class CachedResultServiceImpl implements CachedResultService {

    private final MonthDaysRepository monthDaysRepository;
    private final YearMonthsRepository yearMonthsRepository;
    private final YearWeeksRepository yearWeeksRepository;

    @Override
    public YearMonthsDto getCountOfRegistrationsByMonths() {
        Optional<YearMonthsEntity> entity = yearMonthsRepository.findById(BY_MONTH);
        if (entity.isEmpty()) {
            return new YearMonthsDto();
        }
        YearMonthsDto dto = new YearMonthsDto();
        dto.setYear(entity.get().getYear());
        dto.setMonths(entity.get().getMonths());
        return dto;
    }

    @Override
    public YearWeeksDto getCountOfRegistrationsByWeeks() {
        Optional<YearWeeksEntity> entity = yearWeeksRepository.findById(BY_WEEKS);
        if (entity.isEmpty()) {
            return new YearWeeksDto();
        }
        YearWeeksDto dto = new YearWeeksDto();
        dto.setYear(entity.get().getYear());
        dto.setWeeks(entity.get().getWeeks());
        return dto;
    }

    public MonthDaysDto getCountOfRegistrationsByDays(int month) {
        Optional<MonthDaysEntity> entity = monthDaysRepository.findById(BY_DAY + month);
        if (entity.isEmpty()) {
            return new MonthDaysDto();
        }
        MonthDaysDto dto = new MonthDaysDto();
        dto.setYear(entity.get().getYear());
        dto.setMonth(entity.get().getMonth());
        dto.setDays(entity.get().getDays());
        return dto;
    }

    @Override
    public List<List<Object>> getCountOfRegistrationsByWeeksParsed() {
        var d = getCountOfRegistrationsByWeeks().getWeeks();
        var x = new ArrayList<List<Object>>();

        var t = new ArrayList<Object>();
        t.add("Weeks");
        t.add(TEXT_GITHUB_USERS);
        t.add(TEXT_GITHUB_ORGS);
        x.add(t);

        for (Integer e : d.keySet()) {
            var y = new ArrayList<Object>();
            y.add(e.toString());
            y.addAll(d.get(e));
            x.add(y);
        }
        return x;
    }

    @Override
    public List<List<Object>> getCountOfRegistrationsByMonthsParsed() {
        var d = getCountOfRegistrationsByMonths().getMonths();
        var x = new ArrayList<List<Object>>();

        var t = new ArrayList<Object>();
        t.add("Months");
        t.add(TEXT_GITHUB_USERS);
        t.add(TEXT_GITHUB_ORGS);
        x.add(t);

        for (Integer e : d.keySet()) {
            var y = new ArrayList<Object>();
            y.add(e.toString());
            y.addAll(d.get(e));
            x.add(y);
        }
        return x;
    }

    @Override
    public List<List<Object>> getCountOfRegistrationsByDaysParsed(int month) {
        var d = getCountOfRegistrationsByDays(month).getDays();
        var x = new ArrayList<List<Object>>();

        var t = new ArrayList<Object>();
        t.add("Days");
        t.add(TEXT_GITHUB_USERS);
        t.add(TEXT_GITHUB_ORGS);
        x.add(t);

        for (Integer e : d.keySet()) {
            var y = new ArrayList<Object>();
            y.add(e.toString());
            y.addAll(d.get(e));
            x.add(y);
        }
        return x;
    }
}
