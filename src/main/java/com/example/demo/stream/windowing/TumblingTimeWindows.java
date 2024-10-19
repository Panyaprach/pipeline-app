package com.example.demo.stream.windowing;

import com.example.demo.stream.trigger.TimeTrigger;
import com.example.demo.stream.trigger.Trigger;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;

public class TumblingTimeWindows implements WindowAssigner<Object> {
    private final long size;
    private final long globalOffset;
    private final WindowStagger windowStagger;
    private Long staggerOffset = null;

    private TumblingTimeWindows(long size, long offset, WindowStagger windowStagger) {
        if (Math.abs(offset) >= size)
            throw new IllegalArgumentException("TumblingTimeWindows parameters must satisfy abs(offset) < size");

        this.size = size;
        this.globalOffset = offset;
        this.windowStagger = windowStagger;
    }

    public static TumblingTimeWindows of(Duration size) {
        return new TumblingTimeWindows(size.toMillis(), 0, WindowStagger.ALIGNED);
    }

    public static TumblingTimeWindows of(Duration size, Duration offset) {
        return new TumblingTimeWindows(size.toMillis(), offset.toMillis(), WindowStagger.ALIGNED);
    }

    public static TumblingTimeWindows of(Duration size, Duration offset, WindowStagger windowStagger) {
        return new TumblingTimeWindows(size.toMillis(), offset.toMillis(), windowStagger);
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public Collection<TimeWindow> assignWindows(Object element, long timestamp) {
        final long now = System.currentTimeMillis();
        if (staggerOffset == null) {
            staggerOffset = windowStagger.getStaggerOffset(now, size);
        }
        long start = TimeWindow.getWindowStartWithOffset(now, (globalOffset + staggerOffset) % size, size);

        return Collections.singletonList(new TimeWindow(start, start + size));
    }

    @Override
    public Trigger<Object> getDefaultTrigger() {
        return TimeTrigger.create();
    }
}
