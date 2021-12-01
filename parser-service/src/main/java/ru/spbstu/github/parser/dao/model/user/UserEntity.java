package ru.spbstu.github.parser.dao.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document("user")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserEntity {

    @Id
    private long id;

    private String login;
}
