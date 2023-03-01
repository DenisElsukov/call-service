package com.cs.consumer.processor;

import com.cs.consumer.redis.CallRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCallProcessor implements EventProcessor<String>{
    private final CallRepositoryImpl repository;
    @Override
    public void process(final String callId) {
        repository.delete(callId);
    }
}
