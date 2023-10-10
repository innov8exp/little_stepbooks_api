package net.stepbooks.application.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "contentClient")
public interface ContentClient {


}
