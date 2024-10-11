package com.example.demo.stream;

import java.util.Objects;

public class StreamConsumer<T> {
    private ReferencePipeline pipeline;

    void setPipeline(ReferencePipeline pipeline) {
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
