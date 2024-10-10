package com.example.demo.stream;

import java.util.Objects;

public abstract class AbstractPipeline<E_IN, E_OUT> {
    private static final String MSG_CONSUMED = "source already consumed or closed";

    @SuppressWarnings("rawTypes")
    private final AbstractPipeline sourceStage;
    @SuppressWarnings("rawTypes")
    private final AbstractPipeline previousStage;
    private final int depth;

    public AbstractPipeline() {
        this.previousStage = null;
        this.sourceStage = this;
        this.depth = 0;
    }

    AbstractPipeline(AbstractPipeline<?, E_IN> previousStage) {
        this.previousStage = previousStage;
        this.sourceStage = previousStage.sourceStage;
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

}
