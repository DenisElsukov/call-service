package com.cs.producer.inbound.controller;

import org.openapitools.model.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CallRestService implements RestService {
    private static final Logger LOG = LoggerFactory.getLogger(CallRestService.class);
    KafkaProducer kafkaProducer;

    ExecutorService executor = Executors.newCachedThreadPool();

    public ResponseEntity<String> createCall(Call call) {
        try {
            kafkaProducer.sendToBroker(call);

            return new ResponseEntity<>("Call has been created. Call is " + call.getId(), HttpStatus.CREATED);
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("Invalid data" + ex);
        }
    }
}
