package net.stepbooks.infrastructure.external.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "contentClient")
public interface ContentClient {


}
