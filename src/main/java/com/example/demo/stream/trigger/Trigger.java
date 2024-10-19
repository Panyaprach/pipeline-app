package com.example.demo.stream.trigger;

import com.example.demo.stream.windowing.TimeWindow;
import com.example.demo.stream.windowing.WindowContext;

public interface Trigger<T> {

    TriggerResult onElement(T element, long timestamp, WindowContext<T> ctx);

    TriggerResult onTime(long time, TimeWindow window, WindowContext<T> ctx);
}
