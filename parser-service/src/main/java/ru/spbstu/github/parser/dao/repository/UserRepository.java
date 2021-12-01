package ru.spbstu.github.parser.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spbstu.github.parser.dao.model.user.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, Long> {

    boolean existsByLogin(String login);

    @Query("SELECT login FROM UserEntity u")
     List<String> findAllLogins();
}
