package com.cs.producer.rest.service;

import com.cs.producer.kafka.KafkaProducer;
import com.cs.producer.util.converter.AvroConverter;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.openapitools.model.Call;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class CallRestService implements RestService<Call> {
    private final KafkaProducer kafkaProducer;

    @Override
    public ResponseEntity<String> create(Call call) {
        SpecificRecord createCall = AvroConverter.convertCreateCallToAvro(call);
        kafkaProducer.sendRecordToBroker(createCall);
        return new ResponseEntity<>("Create call request has been processed", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> delete(String callId) {
        SpecificRecord deleteCall = AvroConverter.convertDeleteCallToAvro(callId);
        kafkaProducer.sendRecordToBroker(deleteCall);
        return new ResponseEntity<>("Delete call request has been processed", HttpStatus.OK);
    }
}
