package com.example.demo;

import com.example.demo.stream.Pipeline;
import com.example.demo.stream.StreamConsumer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class KinesisSpringApplication {

    public static void main(String[] args) {
//        SpringApplication.run(KinesisSpringApplication.class, args);
        StreamConsumer<String> source = new StreamConsumer<>();

        Pipeline.of(source)
                .filter(Objects::nonNull)
                .map(String::hashCode)
                .process(System.out::println);
        source.collect("a");
        source.collect("b");
        source.collect(null);
        source.collect("c");
        source.collect("d");
        source.collect("e");
        source.collect(null);

    }


}
