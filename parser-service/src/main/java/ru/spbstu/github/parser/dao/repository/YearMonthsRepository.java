package ru.spbstu.github.parser.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.github.parser.dao.model.result.YearMonthsEntity;

@Repository
public interface YearMonthsRepository extends MongoRepository<YearMonthsEntity, String> {
}
