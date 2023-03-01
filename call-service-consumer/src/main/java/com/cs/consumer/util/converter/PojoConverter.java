package com.cs.consumer.util.converter;

import avro.org.openapitools.model.CreateCall;
import avro.org.openapitools.model.DeleteCall;
import org.openapitools.model.Call;
import org.openapitools.model.Participant;

import java.time.OffsetDateTime;

public class PojoConverter {

    private PojoConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static Call convertCreateCallToPojo(CreateCall createCall) {
        avro.org.openapitools.model.Participant avroParticipant = createCall.getParticipant();
        Participant pojoParticipant = new Participant();
        pojoParticipant.setId(avroParticipant.getId().toString());
        pojoParticipant.setName(avroParticipant.getName().toString());

        Call pojoCall = new Call();
        pojoCall.setId(createCall.getId().toString());
        pojoCall.setCalledNumber(createCall.getCalledNumber().toString());
        pojoCall.setCallerNumber(createCall.getCallerNumber().toString());
        pojoCall.setEngagementDialogId(createCall.getEngagementDialogId().toString());
        pojoCall.setParticipant(pojoParticipant);
        pojoCall.setTimestamp(OffsetDateTime.parse(createCall.getTimestamp().toString()));

        return pojoCall;
    }

    public static String convertDeleteCallToString(DeleteCall deleteCall) {
        return deleteCall.getId().toString();
    }
}
