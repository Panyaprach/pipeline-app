package com.example.demo.stream;

import java.util.Objects;

public abstract class AbstractPipeline<E_IN, E_OUT> {
    @SuppressWarnings("rawtypes")
    private final AbstractPipeline previousStage;
    private final int depth;
    @SuppressWarnings("rawtypes")
    protected AbstractPipeline nextStage;

    public AbstractPipeline() {
        this.previousStage = null;
        this.depth = 0;
    }

    AbstractPipeline(AbstractPipeline<?, E_IN> previousStage) {
        this.previousStage = previousStage;
        this.depth = previousStage.depth + 1;
    }

    abstract Sink<E_IN> opWrapSink(Sink<E_OUT> sink);

    @SuppressWarnings("unchecked")
    final <P_IN> Sink<P_IN> wrapSink(Sink<E_OUT> sink) {
        Objects.requireNonNull(sink);

        //noinspection DataFlowIssue
        for (@SuppressWarnings("rawtypes") AbstractPipeline p = this; p.depth > 0; p = p.previousStage) {
            sink = p.opWrapSink(sink);
        }

        return (Sink<P_IN>) sink;
    }

    @SuppressWarnings("unchecked")
    final <P_IN> Sink<P_IN> getSink() {
        @SuppressWarnings("rawtypes") AbstractPipeline p = this;

        while (p.nextStage != null)
            p = p.nextStage;

        Sink<E_OUT> sink = p.wrapSink(u -> {
        });

        return (Sink<P_IN>) sink;
    }

}
