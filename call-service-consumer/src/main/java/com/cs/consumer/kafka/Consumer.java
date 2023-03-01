package com.cs.consumer.kafka;

import com.cs.consumer.handler.EventOrchestrator;
import io.confluent.parallelconsumer.ParallelStreamProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class Consumer {
    private final ParallelStreamProcessor<String, SpecificRecord> parallelConsumer;
    private final EventOrchestrator<SpecificRecord> handler;

    @Bean
    public void consume() {
        parallelConsumer.poll(specificRecord -> {
            log.info("Record has been consumed: [{}]", specificRecord);

            handler.handle(specificRecord.value());
        });

    }
}
