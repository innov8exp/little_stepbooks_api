package net.stepbooks.infrastructure.external.client;

import net.stepbooks.infrastructure.external.dto.WechatGetAccessTokenResponse;
import net.stepbooks.infrastructure.external.dto.WechatLoginResponse;
import net.stepbooks.infrastructure.external.dto.WechatPhoneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wechatClient", url = "${wechat.host}")
public interface WechatClient {

    @GetMapping(value = "/sns/jscode2session")
    WechatLoginResponse wechatLogin(
            @RequestParam("appid") String appId,
            @RequestParam("secret") String secret,
            @RequestParam("js_code") String jsCode,
            @RequestParam("grant_type") String grantType);

    @GetMapping(value = "/cgi-bin/token")
    WechatGetAccessTokenResponse getAccessToken(
            @RequestParam("appid") String appId,
            @RequestParam("secret") String secret,
            @RequestParam("grant_type") String grantType);

    @PostMapping(value = "cgi-bin/stable_token")
    WechatGetAccessTokenResponse getStableAccessToken(
            @RequestParam("appid") String appId,
            @RequestParam("secret") String secret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("force_refresh") Boolean forceRefresh);

    @PostMapping(value = "/wxa/business/getuserphonenumber")
    WechatPhoneResponse getPhoneNumber(
            @RequestParam("access_token") String accessToken,
            @RequestParam("code") String code,
            @RequestParam("openid") String openId);
}
