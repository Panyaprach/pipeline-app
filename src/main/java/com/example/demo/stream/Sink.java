package com.example.demo.stream;

import java.util.Objects;
import java.util.function.Consumer;

interface Sink<T> extends Consumer<T> {

    @SuppressWarnings("ClassEscapesDefinedScope")
    abstract class ChainedReference<T, E_OUT> implements Sink<T> {
        protected final Sink<? super E_OUT> downstream;

        public ChainedReference(Sink<? super E_OUT> downstream) {
            this.downstream = Objects.requireNonNull(downstream);
        }
    }
}
