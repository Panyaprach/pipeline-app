package com.example.demo.stream.windowing;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

abstract class AbstractTimeWindowStream<T> implements WindowStream<T>{
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    protected final WindowAssigner<? super T> assigner;

    protected AbstractTimeWindowStream(WindowAssigner<? super T> assigner) {
        this.assigner = assigner;

        long delay = assigner.getSize() - System.currentTimeMillis() % assigner.getSize();

        service.scheduleAtFixedRate(this::onTime, delay, assigner.getSize(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void collect(T element) {
        onElement(element);
    }

    @Override
    public void close() {
        service.close();
    }

    abstract void onElement(T element);
    abstract void onTime();
    abstract void emitWindow(TimeWindow window, List<T> contents);
}
