package ru.spbstu.github.executor;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        Executor executor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10));

        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }

        System.out.println(Thread.currentThread().getName());
    }
}
