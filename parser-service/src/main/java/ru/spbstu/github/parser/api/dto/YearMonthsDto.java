package ru.spbstu.github.parser.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class YearMonthsDto {

    private int year;
    private Map<Integer, Integer> months = new HashMap<>();

//    private int january;
//    private int february;
//    private int march;
//    private int april;
//    private int may;
//    private int june;
//    private int july;
//    private int august;
//    private int september;
//    private int october;
//    private int november;
//    private int december;
}
