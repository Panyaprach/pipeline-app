package com.example.demo.stream;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class ReferencePipeline<P_IN, P_OUT> extends AbstractPipeline<P_IN, P_OUT> implements Pipeline<P_OUT> {

    public ReferencePipeline() {
        super();
    }

    public ReferencePipeline(AbstractPipeline<?, P_IN> upstream) {
        super(upstream);
    }

    @Override
    public <R> Pipeline<R> map(Function<? super P_OUT, ? extends R> mapper) {
        Objects.requireNonNull(mapper);

        var op = new StatelessOperator<P_OUT, R>(this) {
            @Override
            Sink<P_OUT> opWrapSink(Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) {
                    public void accept(P_OUT u) {
                        this.downstream.accept(mapper.apply(u));
                    }
                };
            }
        };

        nextStage = op;
        return op;
    }

    @Override
    public Pipeline<P_OUT> filter(Predicate<? super P_OUT> predicate) {
        Objects.requireNonNull(predicate);

        var op = new StatelessOperator<P_OUT, P_OUT>(this) {
            @Override
            Sink<P_OUT> opWrapSink(Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) {

                    @Override
                    public void accept(P_OUT u) {
                        if (predicate.test(u)) {
                            downstream.accept(u);
                        }
                    }
                };
            }
        };
        nextStage = op;

        return op;
    }

    @Override
    public Pipeline<P_OUT> process(Consumer<P_OUT> action) {
        Objects.requireNonNull(action);

        var op = new StatelessOperator<P_OUT, P_OUT>(this) {
            @Override
            Sink<P_OUT> opWrapSink(Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) {

                    @Override
                    public void accept(P_OUT u) {
                        action.andThen(downstream).accept(u);
                    }
                };
            }
        };
        nextStage = op;
        return op;
    }

    abstract static class StatelessOperator<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {

        public StatelessOperator(AbstractPipeline<?, E_IN> upstream) {
            super(upstream);
        }

    }

    static class Head<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {

        @Override
        Sink<E_IN> opWrapSink(Sink<E_OUT> sink) {
            throw new UnsupportedOperationException();
        }
    }
}
