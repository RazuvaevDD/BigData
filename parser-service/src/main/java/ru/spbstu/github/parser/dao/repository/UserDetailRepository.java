package ru.spbstu.github.parser.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.github.parser.dao.model.user.UserDetailEntity;

import java.time.Instant;

@Repository
public interface UserDetailRepository extends MongoRepository<UserDetailEntity, Long> {

    int countUserDetailByCreatedAtIsBetween(Instant from, Instant to);

    int countByCreatedAtBetweenAndType(Instant from, Instant to, String type);
}
