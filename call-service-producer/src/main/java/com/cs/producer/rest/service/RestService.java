package com.cs.producer.rest.service;

import org.springframework.http.ResponseEntity;

public interface RestService<T> {
    ResponseEntity<String> create(T t);

    ResponseEntity<String> delete(String callId);
}
