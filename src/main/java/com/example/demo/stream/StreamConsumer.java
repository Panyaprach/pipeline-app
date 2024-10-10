package com.example.demo.stream;

public interface StreamConsumer<T> {

    void collect(T item);
}
