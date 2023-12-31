package net.stepbooks.infrastructure.external.client;

import net.stepbooks.interfaces.client.dto.GoogleUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleClient", url = "${google.host}")
public interface GoogleClient {

    @GetMapping(value = "/tokeninfo")
    GoogleUserDto introspectGoogle(
            @RequestParam("id_token") String idToken);

}
