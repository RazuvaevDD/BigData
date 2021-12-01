package ru.spbstu.github.parser.service;

import ru.spbstu.github.parser.dao.model.user.UserDetailEntity;

public interface UserService {

    UserDetailEntity retrieve(String login, String token);

    void retrieveAllShortData(long from, long to, long count, int threads);

    void retrieveAllDetailData(long from, long to, int threads);
}
