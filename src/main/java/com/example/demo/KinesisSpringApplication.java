package com.example.demo;

import com.example.demo.stream.InfiniteSource;
import com.example.demo.stream.Pipeline;
import com.example.demo.stream.trigger.CountTimingTrigger;
import com.example.demo.stream.windowing.TimeWindow;
import com.example.demo.stream.windowing.WindowStream;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@Slf4j
public class KinesisSpringApplication {

    public static void main(String[] args) throws Exception {
        InfiniteSource<String> source = new InfiniteSource<>();
        BiConsumer<TimeWindow, List<Integer>> printer = (w, l) -> log.info("{}: {}", w, l);
        WindowStream<Integer> window = WindowStream.of(Duration.ofSeconds(2), printer);
        window.trigger(CountTimingTrigger.of(3));

        Pipeline.of(source)
                .filter(Objects::nonNull)
                .map(String::hashCode)
                .apply(window::collect);

        log.info("Start consuming");
        source.collect("a");
        source.collect("b");
        source.collect(null);
        source.collect("c");
        source.collect("d");
        source.collect("e");
        source.collect(null);

        Thread.sleep(2000);
        window.close();
        log.info("Done");
    }


}
