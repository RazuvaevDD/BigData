package ru.spbstu.github.parser.dao.model;

import lombok.Getter;

@Getter
public enum AccountType {
    USER("UserEntity"),
    ORGANIZATION("Organization");

    private final String name;

    AccountType(String name) {
        this.name = name;
    }
}
