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
import ru.spbstu.github.parser.dao.model.user.UserDetailEntity;
import ru.spbstu.github.parser.dao.model.user.UserEntity;
import ru.spbstu.github.parser.dao.repository.UserDetailRepository;
import ru.spbstu.github.parser.dao.repository.UserRepository;
import ru.spbstu.github.parser.service.UserService;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    public UserDetailEntity retrieve(String login, String token) {
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
        return modelMapper.map(user, UserDetailEntity.class);
    }

    @Override
    public void retrieveAllDetailData(long from, long to, int threads) {
        long usersCount = to - from;
        final long start = from;

        // About 4GB (for speed), use -Xmx4g

        int threadsCount = 10; // Math.min(threads, MAX_THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        // threads = 10
        int requestSize = (int) (usersCount / threadsCount); // 10 000 000 / 10 = 1 000 000

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
        log.error("{} started and will process from {} to {} users", Thread.currentThread().getName(),
                start + from, start + to);

        long startTime = System.currentTimeMillis();
        long finishTime = startTime + 3_600_000; // 1 hour
        long sleepTime;
        int counter = 0;

        for (int i = from; i < to; i++) {
            Long id = start + i;
            final Optional<UserEntity> user = userRepository.findById(id);
            String login = user.map(UserEntity::getLogin).orElse(null);

            UserDetailEntity result = retrieve(login, token);
            counter++;

            if (result != null)
                userDetailRepository.save(result);

            if (counter == 4_999) {
                try {
                    sleepTime = finishTime - System.currentTimeMillis();
                    log.warn("Thread want sleep. Sleep time: {} minutes", TimeUnit.MILLISECONDS.toMinutes(sleepTime));
                    Thread.sleep(sleepTime);

                    // update variables
                    counter = 0;
                    startTime = System.currentTimeMillis();
                    finishTime = startTime + 3_600_000; // 1 hour
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.warn("{} stopped. Thank you!", Thread.currentThread().getName());
    }

    // 5000 requests per 1 hour
    // 40 tokens
    // Total: 5000 * 40 = 50 000 requests per 1 hour

    // from 80 000
    // to 90 000
    @Override
    public void retrieveAllShortData(long from, long to, long count, int threads) {
        long usersCount = to - from;
        final long start = from;
        int threadsCount = 10; // Math.min(threads, MAX_THREAD_COUNT);
        int requestSize = (int) (usersCount / threadsCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        long fromIndex = 0;
        long toIndex = 0;

        for (int i = 1; i <= threadsCount; i++) { // threadsCount = 10
            fromIndex = toIndex;
            toIndex = (long) i * requestSize;

            // for optimize
            long finalFromIndex = start + fromIndex;
            long finalToIndex = start + toIndex;
            int index = i - 1;

            executor.submit(() -> retrieve(finalFromIndex, finalToIndex, count, TOKENS.get(index)));
        }
    }

    private void retrieve(long from, long to, long count, String token) {
        log.error("{} started and will process from {} to {} users", Thread.currentThread().getName(),
                from, to);

        if (from + count - 1 >= to) {
            log.warn("{} stopped. Because from + count >= to", Thread.currentThread().getName());
            return;
        }

        int total = 0;
        long fromUserId = from;

        long startTime = System.currentTimeMillis();
        long finishTime = startTime + 3_600_000; // 1 hour
        long sleepTime;
        int counter = 0;

        while (fromUserId < to) {
            ResponseEntity<UserDto[]> response = sendRequestToGetShortGithubUserData(fromUserId, to, count, token);
            counter++;
            total++;

            if (response.getStatusCode() != HttpStatus.OK || Objects.requireNonNull(response.getBody()).length == 0) {
                continue;
            }

            var users = new LinkedList<>(Arrays.asList(response.getBody()));

            var storedUsers = users.stream()
                    .map(user -> modelMapper.map(user, UserEntity.class))
//                    .peek(user -> log.info("{}: {}", user.getId(), user.getLogin()))
                    .collect(Collectors.toList());

            userRepository.saveAll(storedUsers);

            fromUserId = users.getLast().getId();

            log.info("thread={}, userId={}, requests={}", Thread.currentThread().getName(), fromUserId, total);

            if (counter == 4_999) {
                try {
                    sleepTime = finishTime - System.currentTimeMillis();
                    log.warn("Thread want sleep. Sleep time: {} minutes", TimeUnit.MILLISECONDS.toMinutes(sleepTime));
                    Thread.sleep(sleepTime);

                    // update variables
                    counter = 0;
                    startTime = System.currentTimeMillis();
                    finishTime = startTime + 3_600_000; // 1 hour
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        log.warn("{} stopped.", Thread.currentThread().getName());
    }

    private ResponseEntity<UserDto[]> sendRequestToGetShortGithubUserData(long from, long to, long count, String token) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("since", from);
        parameters.put("per_page", count);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GITHUB_USERS);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", token);

        ResponseEntity<UserDto[]> response = null;

        try {
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    UserDto[].class,
                    Map.of("since", from),
                    Map.of("per_page", to));
        } catch (RestClientException e) {
            log.info("status XXX: from={}, message={}", from, e.getMessage());
        }

        return response;
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
            log.info("status 404: {}", login);
        }
        return response;
    }
}
