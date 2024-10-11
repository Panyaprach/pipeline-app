package com.example.demo.stream;

import java.util.Objects;

public abstract class AbstractPipeline<E_IN, E_OUT> {
    @SuppressWarnings("rawTypes")
    private final AbstractPipeline previousStage;
    @SuppressWarnings("rawTypes")
    protected AbstractPipeline nextStage;
    private final int depth;

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

        for (@SuppressWarnings("rawTypes") AbstractPipeline p = this; p.depth > 0; p = p.previousStage) {
            sink = p.opWrapSink(sink);
        }

        return (Sink<P_IN>) sink;
    }

    @SuppressWarnings("unchecked")
    final <P_IN> Sink<P_IN> getSink() {
        @SuppressWarnings("rawTypes") AbstractPipeline p = this;

        while(p.nextStage != null)
            p = p.nextStage;

        Sink<E_OUT> sink = u -> {};
        sink = p.wrapSink(sink);

        return (Sink<P_IN>) sink;
    }

}
