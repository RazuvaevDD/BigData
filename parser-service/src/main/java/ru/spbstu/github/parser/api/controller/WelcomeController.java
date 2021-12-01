package ru.spbstu.github.parser.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String main(Model model) {
        return "welcome";
    }

    @GetMapping("/result")
    public String result(Model model) {
        return "result";
    }

    @GetMapping("/result/by-weeks")
    public String resultByWeeks(Model model) {
        return "result-by-weeks";
    }

    @GetMapping("/result/by-months")
    public String resultByMonth(Model model) {
        return "result-by-months";
    }
}
