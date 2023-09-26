package co.botechservices.novlnovl.domain.book.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "contentClient")
public interface ContentClient {


}
