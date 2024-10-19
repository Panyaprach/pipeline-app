package com.example.demo.stream.windowing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class TimeWindow {
    private final long start;
    private final long end;

    public TimeWindow(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public static long getWindowStartWithOffset(long timestamp, long offset, long size) {
        return timestamp - (timestamp - offset + size) % size;
    }
}
