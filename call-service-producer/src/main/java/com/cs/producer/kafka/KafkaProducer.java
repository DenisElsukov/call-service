package com.cs.producer.kafka;

import avro.org.openapitools.model.Participant;
import com.cs.producer.util.converter.AvroConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.openapitools.model.Call;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SuccessCallback;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaProducer {

    private final NewTopic topic;
    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    public void sendToBroker(Call call) {
        ProducerRecord<String, SpecificRecord> producerRecord = new ProducerRecord<>(topic.name(), call.getId(), AvroConverter.convertToAvro(call));
        kafkaTemplate.send(producerRecord).addCallback((SuccessCallback<? super SendResult<String, SpecificRecord>>) result -> {
            RecordMetadata recordMetadata = result.getRecordMetadata();
            log.info("Produced to topic {}, partition {}, offset {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        }, log::warn);
        kafkaTemplate.flush();
    }
}
