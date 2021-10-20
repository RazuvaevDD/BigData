package ru.spbstu.github.parser.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.github.parser.service.GithubUserService;

@RestController
@RequestMapping("users")
@Slf4j
public class GithubUserController {

    private final GithubUserService githubUserService;

    @Autowired
    public GithubUserController(GithubUserService githubUserService) {
        this.githubUserService = githubUserService;
    }

    @GetMapping
    public String retrieveUsersShortData(@RequestParam long from, @RequestParam long to, @RequestParam int threads) {
        long count = 100;
        githubUserService.retrieveAllShortData(from, to, count, threads);
        log.info("Parsing to retrieve GitHub users short data started...");
        return "Parsing to retrieve GitHub users short data started...";
    }

    @GetMapping("/detail")
    public String retrieveUsersDetailData(@RequestParam long from, @RequestParam long to, @RequestParam int threads) {
        long count = 100;
        githubUserService.retrieveAllDetailData(from, to, count, threads);
        log.info("Parsing to retrieve GitHub users detail data started...");
        return "Parsing to retrieve GitHub users detail data started...";
    }
}
