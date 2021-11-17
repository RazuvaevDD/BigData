package ru.spbstu.github.parser.dao.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.Instant;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserDetailEntity {

    @Id
    private long id;

    private String login;

    private String type;

    private String name;

    private String company;

    private String blog;

    private String location;

    private String email;

    private String hireable;

    private String bio;

    private String twitterUsername;

    private int publicRepos;

    private int publicGists;

    private int followers;

    private int following;

    private Instant createdAt;

    private Instant updatedAt;
}
