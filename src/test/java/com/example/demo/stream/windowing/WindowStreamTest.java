package com.example.demo.stream.windowing;

import com.example.demo.stream.BiConsumerTestSupport;
import com.example.demo.stream.ConsumerTestSupport;
import com.example.demo.stream.trigger.CountTimingTrigger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class WindowStreamTest {


    ConsumerTestSupport<List<Integer>> consumer;

    WindowStream<Integer> window;

    @BeforeEach
    public void setup() {
        consumer = spy(new ConsumerTestSupport<>());
        window = WindowStream.of(Duration.ofSeconds(1), consumer);
    }

    @AfterEach
    public void teardown() throws Exception {
        window.close();
    }

    @Test
    void whenTimeTrigger_thenOk() throws InterruptedException {
        window.collect(1);
        window.collect(2);
        window.collect(3);

        Thread.sleep(2000);
        verify(consumer).accept(argThat(l -> l.size() == 3));
    }

    @Test
    void whenCountTimingTrigger_thenOk() throws InterruptedException {
        window.trigger(CountTimingTrigger.of(2));

        window.collect(1);
        window.collect(3);
        window.collect(2);

        Thread.sleep(2000);
        verify(consumer, times(2)).accept(anyList());
    }

    @Test
    void givenBiConsumer_whenTimeTrigger_thenOk() throws InterruptedException {
        BiConsumerTestSupport<TimeWindow, List<Integer>> consumer = spy(new BiConsumerTestSupport<>());
        WindowStream<Integer> window = WindowStream.of(Duration.ofSeconds(1), consumer);

        window.collect(1);
        window.collect(2);
        window.collect(3);

        Thread.sleep(2000);
        verify(consumer).accept(any(TimeWindow.class), argThat(l -> l.size() == 3));
    }
}