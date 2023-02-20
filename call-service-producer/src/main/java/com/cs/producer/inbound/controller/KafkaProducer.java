package com.cs.producer.inbound.controller;

import avro.org.openapitools.model.Participant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.openapitools.model.Call;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaProducer {

    // todo: change value type
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private final NewTopic topic;

    public Map<String, Object> producerConfiguration() {
        Map<String, Object> properties = new HashMap<>(kafkaProperties.buildProducerProperties());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return properties;
    }

    @PostConstruct
    ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfiguration());
    }

    public void serealizeParticipantToJSON(org.openapitools.model.Participant participant) {

        Participant avroParticipant = new Participant(participant.getId(), participant.getName());
        DatumWriter<Participant> writer = new SpecificDatumWriter<>(Participant.class);
        try (DataFileWriter<Participant> dataFileWriter = new DataFileWriter<>(writer)) {
            dataFileWriter.create(avroParticipant.getSchema(), new File("participant.avro"));
            dataFileWriter.append(avroParticipant);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserealizeParticipant() {
        DatumReader<Participant> reader = new SpecificDatumReader<>(Participant.class);
        try (DataFileReader<Participant> dataFileReader = new DataFileReader<>(new File("participant.avro"), reader)) {
            Participant participant = null;
            while (dataFileReader.hasNext()) {
                participant = dataFileReader.next(participant);
                log.info("participant: " + participant);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToBroker(Call call) {
        kafkaTemplate.send(topic.name(), "key", "The first message").addCallback((SuccessCallback<? super SendResult<String, String>>) result -> {
            RecordMetadata recordMetadata = result.getRecordMetadata();
            log.info("Produced to topic {}, partition {}, offset {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        }, log::warn);
        kafkaTemplate.flush();
    }
}
