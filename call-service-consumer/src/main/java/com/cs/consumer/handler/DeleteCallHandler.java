package com.cs.consumer.handler;

import avro.org.openapitools.model.DeleteCall;
import com.cs.consumer.processor.DeleteCallProcessor;
import com.cs.consumer.util.converter.PojoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCallHandler implements EventHandler<DeleteCall>{
    private final DeleteCallProcessor processor;

    @Override
    public void handle(final DeleteCall deleteCall) {
        String callId = PojoConverter.convertDeleteCallToString(deleteCall);
        processor.process(callId);
    }
}
