package ru.spbstu.github.parser.dao.model.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Map;
import java.util.UUID;

@Document("months")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class YearWeeksEntity {

    @Id
    private UUID id;

    private int year;

    private Map<Integer, Integer> weeks;
}
