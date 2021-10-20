package ru.spbstu.github.parser.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spbstu.github.parser.service.GithubUserService;

@RestController
@RequestMapping
public class GithubUserController {

    private final GithubUserService githubUserService;

    @Autowired
    public GithubUserController(GithubUserService githubUserService) {
        this.githubUserService = githubUserService;
    }

    @GetMapping("/users")
    public String retrieveUsersInThreads(@RequestParam long from, @RequestParam long to) {
        long count = 100;
        githubUserService.retrieveAll(from, to, count);
        return "Ok";
    }
}
