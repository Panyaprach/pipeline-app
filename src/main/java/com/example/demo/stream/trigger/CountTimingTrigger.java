package com.example.demo.stream.trigger;

import com.example.demo.stream.windowing.TimeWindow;
import com.example.demo.stream.windowing.WindowContext;

public class CountTimingTrigger implements Trigger<Object> {
    private final int maxCount;

    private CountTimingTrigger(int maxCount) {
        this.maxCount = maxCount;
    }

    public static CountTimingTrigger of(int maxCount) {
        return new CountTimingTrigger(maxCount);
    }

    @Override
    public TriggerResult onElement(Object element, long timestamp, WindowContext<Object> ctx) {
        if (ctx.size() == maxCount)
            return TriggerResult.FIRE_AND_PURGE;

        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onTime(long time, TimeWindow window, WindowContext<Object> ctx) {

        return TriggerResult.FIRE;
    }
}
