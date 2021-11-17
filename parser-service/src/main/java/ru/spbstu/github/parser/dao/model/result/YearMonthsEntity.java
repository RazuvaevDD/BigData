package ru.spbstu.github.parser.dao.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Map;

@Document("years")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class YearMonthsEntity {

    @Id
    private int id;

    int year;

    private Map<Integer, Integer> months;
}
