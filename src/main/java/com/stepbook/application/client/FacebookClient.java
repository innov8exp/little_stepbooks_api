package com.stepbook.application.client;

import com.stepbook.interfaces.client.dto.FacebookAuthResDto;
import com.stepbook.interfaces.client.dto.FacebookUserDto;
import com.stepbook.interfaces.client.dto.SocialTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebookClient", url = "${facebook.host}")
public interface FacebookClient {

    @GetMapping(value = "/v12.0/oauth/access_token")
    SocialTokenDto authToFacebook(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType);

    @GetMapping(value = "/debug_token")
    FacebookAuthResDto introspectFacebook(
            @RequestParam("input_token") String inputToken,
            @RequestParam("access_token") String accessToken);

    @GetMapping(value = "/v12.0/{facebookId}")
    FacebookUserDto getFacebookUserInfo(
            @PathVariable("facebookId") String facebookId,
            @RequestParam("fields") String fields,
            @RequestParam("access_token") String accessToken);
}
