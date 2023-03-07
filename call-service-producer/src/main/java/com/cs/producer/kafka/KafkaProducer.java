package com.cs.producer.kafka;

import avro.org.openapitools.model.CreateCall;
import avro.org.openapitools.model.DeleteCall;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaProducer {

    private final NewTopic createCallTopic;
    private final NewTopic deleteCallTopic;
    private final NewTopic defaultTopic;
    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    public void sendRecordToBroker(String key, SpecificRecord specificRecord) {
        NewTopic topic = getTopic(specificRecord);
        ProducerRecord<String, SpecificRecord> producerRecord = new ProducerRecord<>(topic.name(), key, specificRecord);
        ListenableFuture<SendResult<String, SpecificRecord>> future = kafkaTemplate.send(producerRecord);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, SpecificRecord> result) {
                log.info("Sent record: " + producerRecord + " with offset: " + result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send record : " + producerRecord, ex);
            }
        });
    }

    private NewTopic getTopic(SpecificRecord specificRecord) {
        if (specificRecord instanceof CreateCall) {
            return createCallTopic;
        } else if (specificRecord instanceof DeleteCall){
            return deleteCallTopic;
        } else {
            return defaultTopic;
        }
    }
}
