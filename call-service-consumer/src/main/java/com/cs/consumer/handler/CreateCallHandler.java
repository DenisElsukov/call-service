package com.cs.consumer.handler;

import avro.org.openapitools.model.CreateCall;
import com.cs.consumer.processor.CreateCallProcessor;
import com.cs.consumer.util.converter.PojoConverter;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Call;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCallHandler implements EventHandler<CreateCall> {

    private final CreateCallProcessor processor;

    @Override
    public void handle(CreateCall createCall) {
        Call call = PojoConverter.convertCreateCallToPojo(createCall);
        processor.process(call);
    }
}
