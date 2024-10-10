package com.example.demo.stream;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface DataStream<T> extends BaseDataStream<T> {

    static <E> DataStream<E> build() {
        return new ReferencePipeline.Head<>();
    }

    static void main(String[] args) {

        DataStream<String> sample = DataStream.<String>build()
                .filter(Objects::nonNull)
//                .map(String::hashCode)
                .process(System.out::println);
        sample.collect("a");
        sample.collect("b");
        sample.collect(null);
        sample.collect("c");
        sample.collect("d");
    }

    <R> DataStream<R> map(Function<? super T, ? extends R> mapper);

    DataStream<T> filter(Predicate<? super T> predicate);

    DataStream<T> process(Consumer<T> action);
}
