package com.cs.producer.inbound.controller;

import org.openapitools.api.CallsApi;
import org.openapitools.model.Call;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class CallRestController implements CallsApi {

    CallRestService restService;

    public CallRestController(final CallRestService restService) {
        this.restService = restService;
    }

    @Override
    @PostMapping("/calls")
    public ResponseEntity<String> createCall(@RequestBody @Valid final Call call) {
        ResponseEntity responseEntity = null;
        try {
            responseEntity = restService.createCall(call);
        }
        catch (Exception ex) {
            responseEntity = new ResponseEntity<>("Invalid data. " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteCall(final String callId) {
        return CallsApi.super.deleteCall(callId);
    }
}
