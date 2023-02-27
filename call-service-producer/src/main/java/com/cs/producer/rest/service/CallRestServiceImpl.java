package com.cs.producer.rest.service;

import com.cs.producer.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Call;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CallRestServiceImpl implements RestService {
    private final KafkaProducer kafkaProducer;

    // todo: add multithreading
    ExecutorService executor = Executors.newCachedThreadPool();

    public ResponseEntity<String> createCall(Call call) {
        kafkaProducer.sendToBroker(call);

        return new ResponseEntity<>("New call has been created.", HttpStatus.CREATED);
    }
}
