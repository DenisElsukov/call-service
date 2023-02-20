package com.cs.producer.util;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${com.cs.producer.kafka.config.topic.name}")
    String topicName;
    @Value("${com.cs.producer.kafka.config.topic.partitions}")
    Integer numPartitions;
    @Value("${com.cs.producer.kafka.config.topic.replication}")
    Short replicationFactor;
    @Bean
    NewTopic myTopic() {
        return new NewTopic(topicName, numPartitions, replicationFactor);
    }
}
