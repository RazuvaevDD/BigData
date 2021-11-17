package ru.spbstu.github.parser.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class MonthDaysDto {

    private int year;
    private Month month;
    private Map<Integer, Integer> days;
}
