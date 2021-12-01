package ru.spbstu.github.parser.dao.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.Month;
import java.util.List;
import java.util.Map;

@Document("days")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class MonthDaysEntity {

    @Id
    private String id;

    private int year;

    private Month month;

    private Map<Integer, List<Integer>> days;
}
