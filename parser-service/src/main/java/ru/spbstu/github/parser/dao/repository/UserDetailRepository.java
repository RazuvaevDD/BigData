package ru.spbstu.github.parser.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.github.parser.dao.model.UserDetail;

@Repository
public interface UserDetailRepository extends MongoRepository<UserDetail, Long> {
}
