package com.cs.consumer.handler;

public interface EventHandler<T> {
    void handle(T t);
}
