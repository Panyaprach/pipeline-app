package com.example.demo.stream.windowing;

import com.example.demo.stream.trigger.Trigger;

import java.io.Closeable;
import java.time.Duration;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public interface WindowStream<T> extends Closeable {

    static <T> WindowStream<T> of(Duration size, Consumer<List<T>> task) {

        return new TimeWindowStream<>(TumblingTimeWindows.of(size), task);
    }

    static <T> WindowStream<T> of(Duration size, BiConsumer<TimeWindow, List<T>> task) {

        return new TimeWindowStream<>(TumblingTimeWindows.of(size), task);
    }

    void trigger(Trigger<? super T> trigger);

    void collect(T element);
}
