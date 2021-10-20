package ru.spbstu.github.parser.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {

    private long id;

    private String login;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    private String type;
}
