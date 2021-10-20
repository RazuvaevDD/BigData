package ru.spbstu.github.parser.service;

import ru.spbstu.github.parser.dao.model.User;

public interface GithubUserService {

    User retrieve(String login);

    long retrieveFollowersCount(String login);

    void retrieveAllShortData(long from, long to, long count, int threads);

    void retrieveAllDetailData(long from, long to, long count, int threads);
}
