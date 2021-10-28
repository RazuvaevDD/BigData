package ru.spbstu.github.parser.api.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.github.parser.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
@Slf4j
public class GithubUserController {

    private final UserService userService;

    @Autowired
    public GithubUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String retrieveUsersShortData(@RequestParam long from, @RequestParam long to, @RequestParam int threads) {
        long count = 100;
        userService.retrieveAllShortData(from, to, count, threads);
        log.info("Parsing to retrieve GitHub users short data started...");
        return "Parsing to retrieve GitHub users short data started...";
    }

    @GetMapping("/detail")
    public String retrieveUsersDetailData(@RequestParam long from, @RequestParam long to, @RequestParam int threads) {
        userService.retrieveAllDetailData(from, to, threads);
        log.info("Parsing to retrieve GitHub users detail data started...");
        return "Parsing to retrieve GitHub users detail data started...";
    }

    @SneakyThrows
    @GetMapping("/test")
    public String test() {
        long count = 100;
        List<UUID> uuids = new ArrayList<>(10_000_000);

        for (int i = 0; i < 10_000_000; i++) {
            uuids.add(UUID.randomUUID());
        }

        Thread.sleep(5000);

        for (UUID uuid : uuids) {
            System.out.println(uuid);
        }
        return "test";
    }
}
