package com.example.demo.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;

@Slf4j
public class ConsumerTestSupport<T> implements Consumer<T> {
    private CountDownLatch latch;

    public ConsumerTestSupport() {}

    public ConsumerTestSupport(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void accept(T t) {
        if(latch != null)
            latch.countDown();

        log.info("{}", t);
    }
}
