package com.example.demo.stream.windowing;

import com.example.demo.stream.BiConsumerTestSupport;
import com.example.demo.stream.ConsumerTestSupport;
import com.example.demo.stream.trigger.CountTimingTrigger;
import org.apache.commons.lang3.stream.IntStreams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class WindowStreamTest {

    CountDownLatch latch;
    ConsumerTestSupport<List<Integer>> consumer;
    WindowStream<Integer> window;

    @BeforeEach
    public void setup() {
        latch = new CountDownLatch(3);
        consumer = spy(new ConsumerTestSupport<>(latch));
        window = WindowStream.of(Duration.ofSeconds(1), consumer);
    }

    @AfterEach
    public void teardown() throws Exception {
        window.close();
    }

    @Test
    void whenTimeTrigger_thenOk() {
        window.collect(1);
        window.collect(2);
        window.collect(3);

        assertDoesNotThrow(() -> latch.await(2, TimeUnit.SECONDS));
        verify(consumer).accept(argThat(l -> l.size() == 3));
    }

    @Test
    void givenCountTimingTrigger_whenConcurrent_thenOk() {
        int size = 100;
        CountDownLatch latch = new CountDownLatch(size);
        ConsumerTestSupport<List<Integer>> consumer = spy(new ConsumerTestSupport<>(latch));
        WindowStream<Integer> window = WindowStream.of(Duration.ofSeconds(1), consumer);
        window.trigger(CountTimingTrigger.of(10));

        IntStreams.rangeClosed(size).boxed()
                .parallel()
                .forEach(window::collect);

        assertDoesNotThrow(() -> latch.await(2, TimeUnit.SECONDS));
        verify(consumer, times(11)).accept(anyList());
    }

    @Test
    void whenCountTimingTrigger_thenOk() {
        window.trigger(CountTimingTrigger.of(2));

        window.collect(1);
        window.collect(3);
        window.collect(2);

        assertDoesNotThrow(() -> latch.await(2, TimeUnit.SECONDS));
        verify(consumer, times(2)).accept(anyList());
    }

    @Test
    void givenBiConsumer_whenTimeTrigger_thenOk() throws IOException {
        BiConsumerTestSupport<TimeWindow, List<Integer>> consumer = spy(new BiConsumerTestSupport<>(latch));
        try(WindowStream<Integer> window = WindowStream.of(Duration.ofSeconds(1), consumer)) {

            window.collect(1);
            window.collect(2);
            window.collect(3);

            assertDoesNotThrow(() -> latch.await(2, TimeUnit.SECONDS));
            verify(consumer).accept(any(TimeWindow.class), argThat(l -> l.size() == 3));
        }
    }
}