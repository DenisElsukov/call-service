package com.cs.consumer.handler;

import avro.org.openapitools.model.CreateCall;
import avro.org.openapitools.model.DeleteCall;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
public class EventOrchestrator<T extends SpecificRecord> {

    private final Map<Class<? extends SpecificRecord>, EventHandler<T>> map;

    public EventOrchestrator(final CreateCallHandler createCallHandler,
        final DeleteCallHandler deleteCallHandler) {
        map = Map.of(CreateCall.class, (EventHandler<T>)createCallHandler,
            DeleteCall.class, (EventHandler<T>) deleteCallHandler);
    }

    public void handle(final T value) {
        try {
            map.get(value.getClass()).handle(value);
        } catch (NullPointerException ex) {
            log.error("There are no handler for {} type event. {}", value.getClass().getName(), ex.getMessage());
        }
    }
}
