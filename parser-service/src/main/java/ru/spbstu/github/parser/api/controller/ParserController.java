package ru.spbstu.github.parser.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.github.parser.service.UserService;

@RestController
@RequestMapping("users")
@Slf4j
public class ParserController {

    private final UserService userService;

    @Autowired
    public ParserController(UserService userService) {
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
}
