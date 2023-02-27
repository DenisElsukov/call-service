package com.cs.consumer.kafka;

import avro.org.openapitools.model.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "myTopic")
    void listener(Call call) {
        LOG.info("Record has been consumed : " + call);
    }
}
