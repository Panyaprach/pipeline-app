package com.example.demo.stream;

import com.example.demo.stream.windowing.TimeWindow;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

@Slf4j
public class BiConsumerTestSupport<T1, T2> implements BiConsumer<T1, T2> {
    private CountDownLatch latch;

    public BiConsumerTestSupport(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void accept(T1 t1, T2 t2) {
        log.info("{}: {}", t1, t2);
    }
}
