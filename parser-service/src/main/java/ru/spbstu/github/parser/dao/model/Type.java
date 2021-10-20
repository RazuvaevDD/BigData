package ru.spbstu.github.parser.dao.model;

import lombok.Getter;

@Getter
public enum Type {
    USER("User"),
    ORGANIZATION("Organization");

    private final String name;

    Type(String name) {
        this.name = name;
    }
}
