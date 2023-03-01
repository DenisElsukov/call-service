package com.cs.producer.kafka.configuration;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
public class KafkaConfig {

    private final String createCallTopic;
    private final String deleteCallTopic;
    private final Integer numPartitions;
    private final Short replicationFactor;
    private final String bootstrapAddress;
    private final String schemaRegistry;

    @Autowired
    public KafkaConfig(
        @Value("${com.cs.producer.kafka.config.topic.create_call}") final String createCallTopic,
        @Value("${com.cs.producer.kafka.config.topic.delete_call}") final String deleteCallTopic,
        @Value("${com.cs.producer.kafka.config.topic.partitions}") final Integer numPartitions,
        @Value("${com.cs.producer.kafka.config.topic.replication}") final Short replicationFactor,
        @Value("${com.cs.producer.kafka.config.bootstrap_servers}") final String bootstrapAddress,
        @Value("${com.cs.producer.kafka.config.schema_registry}") final String schemaRegistry
    ) {
        this.createCallTopic = createCallTopic;
        this.deleteCallTopic = deleteCallTopic;
        this.numPartitions = numPartitions;
        this.replicationFactor = replicationFactor;
        this.bootstrapAddress = bootstrapAddress;
        this.schemaRegistry = schemaRegistry;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic createCallTopic() {
        return new NewTopic(createCallTopic, numPartitions, replicationFactor);
    }
    @Bean
    NewTopic deleteCallTopic() {
        return new NewTopic(deleteCallTopic, numPartitions, replicationFactor);
    }

    @Bean
    public ProducerFactory<String, SpecificRecord> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    KafkaTemplate<String, SpecificRecord> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
