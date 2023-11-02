package net.stepbooks.infrastructure.external.client;

import net.stepbooks.infrastructure.external.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "smsClient", url = "${sms.host}", fallback = SmsFailedResponse.class)
public interface SmsClient {

    @GetMapping(value = "/balance")
    ResponseEntity<SmsBalanceResponse> balance(@RequestBody SmsBalanceRequest request);

    @PostMapping(value = "/send")
    ResponseEntity<SmsSendResponse> send(@RequestBody SmsSendRequest request);
}

