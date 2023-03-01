package com.cs.consumer.redis;

import org.openapitools.model.Call;

public interface CallRepository {
    void save(Call call);
    void delete(String callId);
}
