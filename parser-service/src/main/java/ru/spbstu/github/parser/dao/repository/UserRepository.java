package ru.spbstu.github.parser.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spbstu.github.parser.dao.model.User;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    boolean existsByLogin(String login);

    @Query("SELECT login FROM User u")
     List<String> findAllLogins();
}
