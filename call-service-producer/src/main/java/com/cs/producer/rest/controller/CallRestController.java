package com.cs.producer.rest.controller;

import com.cs.producer.rest.service.CallRestService;
import com.cs.producer.rest.service.RestService;
import lombok.extern.log4j.Log4j2;
import org.openapitools.api.CallsApi;
import org.openapitools.model.Call;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@Log4j2
@Validated
public class CallRestController implements CallsApi {

    RestService<Call> restService;

    public CallRestController(final CallRestService restService) {
        this.restService = restService;
    }

    @Override
    @PostMapping("/calls")
    public ResponseEntity<String> createCall(@RequestBody @Valid final Call call) {
        log.info("createCall() [{}] >>> Received create call request. Call [{}]", call.getId(), call);
        return restService.create(call);
    }

    @Override
    @PostMapping("/calls/{callId}")
    public ResponseEntity<String> deleteCall(@PathVariable final String callId) {
        log.info("deleteCall() >>> Received delete call request for callId [{}]", callId);
        return restService.delete(callId);
    }
}
