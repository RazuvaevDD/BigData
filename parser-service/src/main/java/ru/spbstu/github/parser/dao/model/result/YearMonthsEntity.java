package ru.spbstu.github.parser.dao.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Map;
import java.util.UUID;

@Document("years")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class YearMonthsEntity {

    @Id
    private UUID id;

    int year;

    private Map<Integer, Integer> months;
}
