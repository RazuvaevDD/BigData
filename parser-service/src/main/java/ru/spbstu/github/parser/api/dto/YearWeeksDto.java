package ru.spbstu.github.parser.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class YearWeeksDto {

    private int year;
    private Map<Integer, List<Integer>> weeks;
}
