package com.cs.consumer.processor;

import com.cs.consumer.redis.CallRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openapitools.model.Call;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class CreateCallProcessor implements EventProcessor<Call> {
    private final CallRepositoryImpl repository;
    @Override
    public void process(final Call call) {
        repository.save(call);
        log.info("call {} has been cached into Redisson", call.getId());
    }
}
