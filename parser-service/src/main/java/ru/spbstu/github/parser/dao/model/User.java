package ru.spbstu.github.parser.dao.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    private long id;

    private String login;
}
