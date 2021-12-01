package ru.spbstu.github.parser.dao.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Document("month")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class YearMonthsEntity {

    @Id
    private String id;

    int year;

    private Map<Integer, List<Integer>> months;
}
