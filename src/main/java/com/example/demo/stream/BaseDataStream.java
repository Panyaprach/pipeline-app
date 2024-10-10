package com.example.demo.stream;

public interface BaseDataStream<T> {

    void collect(T element);
}
