package com.cs.consumer.processor;

public interface EventProcessor<T> {
    void process(T t);
}
