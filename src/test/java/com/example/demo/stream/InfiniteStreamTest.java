package com.example.demo.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class InfiniteStreamTest {

    InfiniteSource<Integer> source = new InfiniteSource<>();

    @Test
    void whenFilter_thenOk() {
        Consumer<Integer> sink = spy(new ConsumerTestSupport<>());

        Pipeline.of(source)
                .filter(i -> i % 2 == 0)
                .apply(sink);

        source.collect(1);
        source.collect(2);
        source.collect(3);

        verify(sink).accept(eq(2));
        verify(sink, never()).accept(eq(1));
        verify(sink, never()).accept(eq(3));
    }

    @Test
    void whenMap_thenOk() {
        Consumer<String> sink = spy(new ConsumerTestSupport<>());

        Pipeline.of(source)
                .map(Integer::toHexString)
                .apply(sink);

        source.collect(200);

        verify(sink).accept(eq("c8"));
    }

    @Test
    void whenApply_thenOk() {
        Consumer<Integer> sink = spy(new ConsumerTestSupport<>());

        Pipeline.of(source).apply(sink);

        source.collect(1);
        source.collect(2);
        source.collect(3);

        verify(sink).accept(eq(1));
        verify(sink).accept(eq(2));
        verify(sink).accept(eq(3));
    }

    @Test
    void whenProcess_thenOk() {
        Consumer<Integer> sink = spy(new ConsumerTestSupport<>());

        Pipeline.of(source)
                .process(sink)
                .apply(sink);

        source.collect(1);
        source.collect(2);
        source.collect(3);

        verify(sink, times(2)).accept(eq(1));
        verify(sink, times(2)).accept(eq(2));
        verify(sink, times(2)).accept(eq(3));
    }

    @Test
    void whenPrint_thenOk() {
        Pipeline.of(source).print();

        assertDoesNotThrow(() -> source.collect(1));
        assertDoesNotThrow(() -> source.collect(2));
        assertDoesNotThrow(() -> source.collect(3));
    }

}