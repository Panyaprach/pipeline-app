package com.example.demo.stream.windowing;

import com.example.demo.stream.trigger.Trigger;

import java.time.Duration;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public interface WindowStream<T> extends AutoCloseable {

    void trigger(Trigger<? super T> trigger);
    void collect(T element);

    public static <T> WindowStream<T> of(Duration size, Consumer<List<T>> task) {

        return new TimeWindowStream<>(TumblingTimeWindows.of(size), task);
    }

    public static <T> WindowStream<T> of(Duration size, BiConsumer<TimeWindow, List<T>> task) {

        return new TimeWindowStream<>(TumblingTimeWindows.of(size), task);
    }
}
