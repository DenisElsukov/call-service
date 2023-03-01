package com.cs.producer.util.converter;

import avro.org.openapitools.model.DeleteCall;
import avro.org.openapitools.model.Participant;
import org.openapitools.model.Call;

public class AvroConverter {

    private AvroConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static avro.org.openapitools.model.CreateCall convertCreateCallToAvro(Call call) {
        Participant avroParticipant = Participant.newBuilder().setId(call.getParticipant().getId()).setName(call.getParticipant().getName()).build();
        return avro.org.openapitools.model.CreateCall.newBuilder()
            .setId(call.getId())
            .setCalledNumber(call.getCalledNumber())
            .setCallerNumber(call.getCallerNumber())
            .setEngagementDialogId(call.getEngagementDialogId())
            .setParticipant(avroParticipant)
            .setTimestamp(call.getTimestamp().toInstant())
            .build();
    }

    public static DeleteCall convertDeleteCallToAvro(String callId) {
        return DeleteCall.newBuilder()
            .setId(callId)
            .build();
    }
}
