package com.example.demo.stream.trigger;

import com.example.demo.stream.windowing.TimeWindow;
import com.example.demo.stream.windowing.WindowContext;

public class TimeTrigger implements Trigger<Object> {

    private TimeTrigger() {}

    public static TimeTrigger create() {
        return new TimeTrigger();
    }

    @Override
    public TriggerResult onElement(Object element, long timestamp, WindowContext<Object> ctx) {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onTime(long time, TimeWindow window, WindowContext<Object> ctx) {
        return TriggerResult.FIRE;
    }
}
