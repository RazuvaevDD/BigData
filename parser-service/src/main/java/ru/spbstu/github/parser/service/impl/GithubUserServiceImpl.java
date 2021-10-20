package ru.spbstu.github.parser.service.impl;

import ru.spbstu.github.parser.api.dto.UserDto;
import ru.spbstu.github.parser.dao.model.User;
import ru.spbstu.github.parser.dao.repository.UserRepository;
import ru.spbstu.github.parser.service.GithubUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ru.spbstu.github.parser.helper.EndpointHelper.*;

@Slf4j
@Service
@AllArgsConstructor
public class GithubUserServiceImpl implements GithubUserService {

    private final static int MAX_THREAD_COUNT = 40;
    private final static int REQUEST_COUNT = 500_000;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User retrieve(String login) {
        // To be done
        return null;
    }

    @Override
    public long retrieveFollowersCount(String login) {
        // To be done
        return 0;
    }

    // 5000 requests per 1 hour
    // 40 tokens
    // Total: 5000 * 40 = 50 000 requests per 1 hour
    @Override
    public void retrieveAllShortData(long from, long to, long count, int threads) {
        int threadsCount = Math.min(threads, MAX_THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        for (int i = 1; i <= threadsCount; i++) {
            var x = i; // effective final variable
            executor.submit(() -> retrieve(from, from * x + REQUEST_COUNT, count, TOKENS.get(x - 1)));
        }
    }

    @Override
    public void retrieveAllDetailData(long from, long to, long count, int threads) {
        int threadsCount = Math.min(threads, MAX_THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        for (int i = 1; i <= threadsCount; i++) {
            var x = i; // effective final variable
            // TODO: Implement this logic
            //executor.submit(() -> retrieve(from, from * x + REQUEST_COUNT, count, TOKENS.get(x - 1)));
        }
    }

    private void retrieve(long from, long to, long count, String token) {
        if (from + count - 1 >= to) {
            return;
        }
        ResponseEntity<UserDto[]> response = sendRequestToGetShortGithubUserData(from, to, count, token);

        if (response.getStatusCode() != HttpStatus.OK || Objects.requireNonNull(response.getBody()).length == 0) {
            return;
        }

        var userStore = new LinkedList<>(Arrays.asList(response.getBody()));
        var x = userStore.stream()
                .map(user -> modelMapper.map(user, User.class))
                .peek(user -> log.info("{}: {}", user.getId(), user.getLogin()))
//                .filter(user -> !userRepository.existsByLogin(user.getLogin()))
                .collect(Collectors.toList());

        userRepository.saveAll(x);
        var fromUserId = userStore.getLast().getId();
        log.info("thread={}, userId={}", Thread.currentThread().getName(), fromUserId);
        retrieve(fromUserId, to, count, token);
    }

    private ResponseEntity<UserDto[]> sendRequestToGetShortGithubUserData(long from, long to, long count, String token) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("accept", "application/vnd.github.v3+json");
        parameters.put("since", from);
        parameters.put("per_page", count);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GITHUB_USERS);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", token);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserDto[].class,
                Map.of("since", from),
                Map.of("per_page", to),
                Map.of("accept", "application/vnd.github.v3+json"));
    }
}
