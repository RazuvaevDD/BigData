package ru.spbstu.github.parser.dao.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.Month;
import java.util.Map;
import java.util.UUID;

@Document("months")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class MonthDaysEntity {

    @Id
    private UUID id;

    private int year;

    private Month month;

    private Map<Integer, Integer> days;
}
