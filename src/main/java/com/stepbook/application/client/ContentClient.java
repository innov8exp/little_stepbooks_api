package com.stepbook.application.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "contentClient")
public interface ContentClient {


}
