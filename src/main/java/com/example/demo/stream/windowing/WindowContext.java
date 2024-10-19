package com.example.demo.stream.windowing;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class WindowContext<T> {
    final List<T> contents = Collections.synchronizedList(new ArrayList<>());

    public void add(T element) {
        contents.add(element);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public int size() {
        return contents.size();
    }

    public void clear() {
        contents.clear();
    }
}
