package ru.spbstu.github.parser.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.github.parser.dao.model.result.YearWeeksEntity;

@Repository
public interface YearWeeksRepository extends MongoRepository<YearWeeksEntity, Long> {
}
