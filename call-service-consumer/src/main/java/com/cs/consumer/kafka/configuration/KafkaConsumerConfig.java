package com.cs.consumer.kafka.configuration;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.parallelconsumer.ParallelConsumerOptions;
import io.confluent.parallelconsumer.ParallelStreamProcessor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Properties;
import java.util.UUID;

import static io.confluent.parallelconsumer.ParallelConsumerOptions.ProcessingOrder.KEY;
import static pl.tlinkowski.unij.api.UniLists.of;

@EnableKafka
@Configuration
@PropertySource("classpath:application.properties")
public class KafkaConsumerConfig {
    private final String bootstrapAddress;
    private final String schemaRegistry;
    private final String createCallTopic;
    private final String deleteCallTopic;

    @Autowired
    public KafkaConsumerConfig(
        @Value("${com.cs.consumer.kafka.config.bootstrap_servers}") final String bootstrapAddress,
        @Value("${com.cs.consumer.kafka.config.schema_registry}") final String schemaRegistry,
        @Value("${com.cs.consumer.kafka.config.topic.create_call}") final String createCallTopic,
        @Value("${com.cs.consumer.kafka.config.topic.delete_call}") final String deleteCallTopic
    ) {
        this.bootstrapAddress = bootstrapAddress;
        this.schemaRegistry = schemaRegistry;
        this.createCallTopic = createCallTopic;
        this.deleteCallTopic = deleteCallTopic;
    }

    public Properties consumerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.setProperty(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        properties.setProperty(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        return properties;
    }

    KafkaConsumer<String, SpecificRecord> consumer() {
        return new KafkaConsumer<>(consumerProperties());
    }

    @Bean
    ParallelStreamProcessor<String, SpecificRecord> setupParallelConsumer() {
        var options = ParallelConsumerOptions.<String, SpecificRecord>builder().ordering(KEY).maxConcurrency(1000).consumer(consumer()).build();

        ParallelStreamProcessor<String, SpecificRecord> eosStreamProcessor = ParallelStreamProcessor.createEosStreamProcessor(options);
        eosStreamProcessor.subscribe(of(createCallTopic, deleteCallTopic));

        return eosStreamProcessor;
    }

}
