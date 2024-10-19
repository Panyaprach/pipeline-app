package com.example.demo.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class ConsumerTestSupport<T> implements Consumer<T> {

    @Override
    public void accept(T t) {
        log.info("{}", t);
    }
}
