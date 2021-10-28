package ru.spbstu.github.parser.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.spbstu.github.parser.api.dto.UserDetailDto;
import ru.spbstu.github.parser.api.dto.UserDto;
import ru.spbstu.github.parser.dao.model.User;
import ru.spbstu.github.parser.dao.model.UserDetail;
import ru.spbstu.github.parser.dao.repository.UserDetailRepository;
import ru.spbstu.github.parser.dao.repository.UserRepository;
import ru.spbstu.github.parser.service.UserService;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ru.spbstu.github.parser.helper.EndpointHelper.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final static int MAX_THREAD_COUNT = 40;
    private final static int REQUEST_COUNT = 500_000;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    public UserDetail retrieve(String login, String token) {
        if (login == null) return null;
        final ResponseEntity<String> response = sendRequestToGetDetailedUser(login, token);

        if (response == null)
            return null;

        if (response.getStatusCode() != HttpStatus.OK) {
            log.info("status {}: {}", response.getStatusCode(), "NULL");
            return null;
        }

        UserDetailDto user = null;

        try {
            user = objectMapper.readValue(response.getBody(), UserDetailDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (user == null)
            return null;

        log.info("status {}: {}, {}", response.getStatusCode(), user.getId(), user.getLogin());
        return modelMapper.map(user, UserDetail.class);
    }

    @Override
    public void retrieveAllDetailData(long from, long to, int threads) {
        long usersCount = to - from;
        final long start = from;

        // About 4GB (for speed), use -Xmx4g

        int threadsCount = Math.min(threads, MAX_THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        // threads = 10
        int requestSize = (int) (usersCount / threads); // 10 000 000 / 10 = 1 000 000

        int fromIndex = 0;
        int toIndex = 0;

        for (int i = 1; i <= threadsCount; i++) { // threadsCount = 10
            fromIndex = toIndex;
            toIndex = i * requestSize;

            // for optimize
            int finalFromIndex = fromIndex;
            int finalToIndex = toIndex;
            int index = i - 1;

            executor.submit(() -> retrieveByLogins(start, finalFromIndex, finalToIndex, TOKENS.get(index)));
        }
    }

    private void retrieveByLogins(long start, int from, int to, String token) {
//        final Page<User> users = userRepository.findAll(PageRequest.of(from, to, Sort.DEFAULT_DIRECTION));
        for (int i = from; i < to; i++) {
            Long id = start + i;
            final Optional<User> user = userRepository.findById(id);
            String login = user.map(User::getLogin).orElse(null);

            UserDetail result = retrieve(login, token);

            if (result != null)
                userDetailRepository.save(result);

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 5000 requests per 1 hour
    // 40 tokens
    // Total: 5000 * 40 = 50 000 requests per 1 hour
    @Override
    public void retrieveAllShortData(long from, long to, long count, int threads) {
        int threadsCount = Math.min(threads, MAX_THREAD_COUNT);
        int requestCount = Math.min(threads, MAX_THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        int number = 400_000;

        long x1 = from + number;
        long x2 = from + number + number;
        long x3 = from + number + number + number;
        long x4 = from + number + number + number + number;
        long x5 = from + number + number + number + number + number;

        executor.submit(() -> retrieve(from, x1, count, TOKENS.get(0)));
        executor.submit(() -> retrieve(x1, x2, count, TOKENS.get(1)));
        executor.submit(() -> retrieve(x2, x3, count, TOKENS.get(2)));
        executor.submit(() -> retrieve(x3, x4, count, TOKENS.get(3)));
        executor.submit(() -> retrieve(x4, x5, count, TOKENS.get(4)));

//        for (int i = 1; i <= threadsCount; i++) {
//            var x = i; // effective final variable
//            executor.submit(() -> retrieve(from, from * x + REQUEST_COUNT, count, TOKENS.get(x - 1)));
//        }
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

    private ResponseEntity<String> sendRequestToGetDetailedUser(String login, String token) {
        String url = String.format(GITHUB_USER_DETAIL, login);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", token);

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);
        } catch (RestClientException e) {
            log.info("status 404: NOT FOUND");
        }
        return response;
    }
}
