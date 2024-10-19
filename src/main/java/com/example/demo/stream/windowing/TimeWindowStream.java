package com.example.demo.stream.windowing;

import com.example.demo.stream.trigger.Trigger;
import com.example.demo.stream.trigger.TriggerResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

final class TimeWindowStream<T> extends AbstractTimeWindowStream<T> {
    private final HashMap<TimeWindow, WindowContext<T>> state = new HashMap<>();
    private final Consumer<List<T>> task;
    private final BiConsumer<TimeWindow, List<T>> richTask;

    private Trigger<T> trigger;

    TimeWindowStream(WindowAssigner<? super T> assigner, Consumer<List<T>> task) {
        super(assigner);
        this.trigger = (Trigger<T>) assigner.getDefaultTrigger();
        this.task = task;
        this.richTask = null;
    }

    TimeWindowStream(WindowAssigner<? super T> assigner, BiConsumer<TimeWindow, List<T>> richTask) {
        super(assigner);
        this.trigger = (Trigger<T>) assigner.getDefaultTrigger();
        this.task = null;
        this.richTask = richTask;
    }

    @Override
    void onElement(T element) {
        synchronized (this) {
            long now = System.currentTimeMillis();
            Collection<TimeWindow> windows = assigner.assignWindows(element, now);
            for (TimeWindow window : windows) {
                WindowContext<T> ctx = state.computeIfAbsent(window, w -> new WindowContext<T>());
                ctx.add(element);

                TriggerResult triggerResult = trigger.onElement(element, now, ctx);

                if (triggerResult.isFire()) {
                    if (ctx.isEmpty())
                        continue;

                    List<T> contents = ctx.getContents();
                    emitWindow(window, contents);
                }

                if (triggerResult.isPurge())
                    ctx.clear();
            }
        }
    }

    @Override
    void onTime() {
        synchronized (this) {
            if (state.isEmpty())
                return;

            long now = System.currentTimeMillis();
            for (TimeWindow window : state.keySet()) {
                WindowContext<T> ctx = state.get(window);
                TriggerResult triggerResult = trigger.onTime(now, window, ctx);

                if (triggerResult.isFire()) {
                    if (ctx.isEmpty())
                        continue;

                    List<T> contents = ctx.getContents();
                    emitWindow(window, contents);
                }

                if (triggerResult.isPurge())
                    ctx.clear();
            }
        }
    }

    @Override
    void emitWindow(TimeWindow window, List<T> contents) {
        if (task!=null)
            task.accept(contents);
        else
            richTask.accept(window, contents);

        state.remove(window);
    }

    @Override
    public void trigger(Trigger<? super T> trigger) {
        this.trigger = (Trigger<T>) trigger;
    }
}
