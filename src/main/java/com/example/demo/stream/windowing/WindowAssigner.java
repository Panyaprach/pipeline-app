package com.example.demo.stream.windowing;

import com.example.demo.stream.trigger.Trigger;

import java.util.Collection;

public interface WindowAssigner<T> {

    long getSize();

    Collection<TimeWindow> assignWindows(T element, long timestamp);

    Trigger<T> getDefaultTrigger();
}
