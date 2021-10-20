package ru.spbstu.github.parser.service;

import ru.spbstu.github.parser.dao.model.User;

public interface GithubUserService {

    User retrieve(String login);

    long retrieveFollowersCount(String login);

    void retrieveAll(long from, long to, long count);
}
