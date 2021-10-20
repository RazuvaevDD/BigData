package ru.spbstu.github.parser.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.spbstu.github.parser.dao.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    boolean existsByLogin(String login);
}
