package com.stepbook.domain.book.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "contentClient")
public interface ContentClient {


}
