package net.stepbooks.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WechatPhoneResponse {

    @JsonProperty("errcode")
    private Integer errCode;
    @JsonProperty("errmsg")
    private String errMsg;
    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo {
        private String phoneNumber;
        private String purePhoneNumber;
        private String countryCode;
        private Watermark watermark;
    }

    @Data
    public static class Watermark {
        private Long timestamp;
        @JsonProperty("appid")
        private String appId;
    }
}
