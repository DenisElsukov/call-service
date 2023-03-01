package com.cs.consumer.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openapitools.model.Call;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Log4j2
@Repository
public class CallRepositoryImpl implements CallRepository {
    private final RedissonClient redissonClient;
    private final String key = "Call";

    @Override
    public void save(Call call) {
        RMap<String, Call> callsMap = redissonClient.getMap(key);
        callsMap.put(call.getId(), call);
    }

    @Override
    public void delete(String callId) {
        RMap<String, Call> callsMap = redissonClient.getMap(key);
        Call deletedCall = callsMap.remove(callId);
        log.info(deletedCall != null ? "call " + callId + " has been removed from Redis" : "call " + callId + " doesn't exist in Redis");
    }
}
