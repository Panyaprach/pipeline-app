package com.example.demo.stream;

import java.util.Objects;

public class InfiniteSource<T> {
    private ReferencePipeline<T, ?> pipeline;

    void setPipeline(ReferencePipeline<T, ?> pipeline) {
        Objects.requireNonNull(pipeline);

        this.pipeline = pipeline;
    }

    public void collect(T element) {
        if (pipeline == null)
            return;

        Sink<T> sink = pipeline.getSink();
        sink.accept(element);
    }
}
