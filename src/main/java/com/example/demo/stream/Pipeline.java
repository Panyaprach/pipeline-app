package com.example.demo.stream;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Pipeline<T> {

    static <E> Pipeline<E> of(InfiniteSource<E> source) {
        ReferencePipeline.Head head = new ReferencePipeline.Head<>();
        source.setPipeline(head);
        return head;
    }

    <R> Pipeline<R> map(Function<? super T, ? extends R> mapper);

    Pipeline<T> filter(Predicate<? super T> predicate);

    void apply(Consumer<T> action);

    void print();
}
