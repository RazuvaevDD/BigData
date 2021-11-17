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

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CachedResultServiceImpl implements CachedResultService {

    private final MonthDaysRepository monthDaysRepository;
    private final YearMonthsRepository yearMonthsRepository;
    private final YearWeeksRepository yearWeeksRepository;

    @Override
    public YearMonthsDto getCountOfRegistrationsByMonths() {
        final List<YearMonthsEntity> entities = yearMonthsRepository.findAll();
        if (entities.size() == 0) {
            return new YearMonthsDto();
        }
        YearMonthsEntity entity = entities.get(0);
        YearMonthsDto dto = new YearMonthsDto();
        dto.setYear(entity.getYear());
        dto.setMonths(entity.getMonths());
        return dto;
    }

    @Override
    public YearWeeksDto getCountOfRegistrationsByWeeks() {
        final List<YearWeeksEntity> entities = yearWeeksRepository.findAll();
        if (entities.size() == 0) {
            return new YearWeeksDto();
        }
        YearWeeksEntity entity = entities.get(0);
        YearWeeksDto dto = new YearWeeksDto();
        dto.setYear(entity.getYear());
        dto.setWeeks(entity.getWeeks());
        return dto;
    }

    public MonthDaysDto getCountOfRegistrationsByDays(int month) {
        final List<MonthDaysEntity> entities = monthDaysRepository.findAll();
        if (entities.size() == 0) {
            return new MonthDaysDto();
        }
        MonthDaysEntity entity = entities.get(0);
        MonthDaysDto dto = new MonthDaysDto();
        dto.setYear(entity.getYear());
        dto.setMonth(entity.getMonth());
        dto.setDays(entity.getDays());
        return dto;
    }
}
